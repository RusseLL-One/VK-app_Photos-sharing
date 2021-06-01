package com.one.russell.e_app.repository

import android.content.Context
import com.one.russell.e_app.Constants
import com.one.russell.e_app.data.PhotoUploading
import com.one.russell.e_app.data.SaveWallPhoto
import com.one.russell.e_app.data.UploadServerUrl
import com.one.russell.e_app.repository.converters.PhotoUploadingConverter
import com.one.russell.e_app.repository.converters.SaveWallPhotoConverter
import com.one.russell.e_app.repository.converters.UploadServerUrlConverter
import com.one.russell.e_app.repository.entities.PhotoUploadingResponse
import com.one.russell.e_app.repository.entities.SaveWallPhotoResponse
import com.one.russell.e_app.repository.entities.UploadServerUrlResponse
import com.one.russell.e_app.repository.requests.VKGetUploadServerUrlRequest
import com.one.russell.e_app.repository.requests.VKPublishWallPostRequest
import com.one.russell.e_app.repository.requests.VKSaveWallPhotoRequest
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.exceptions.VKApiExecutionException
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url
import java.io.File

class RepositoryImpl : Repository {

    private var api: VKService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.VK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(VKService::class.java)
    }

    override fun saveAccessToken(context: Context, accessToken: VKAccessToken) {
        val sharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        accessToken.save(sharedPreferences)
    }

    override fun getAccessToken(context: Context): VKAccessToken? {
        val sharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        return VKAccessToken.restore(sharedPreferences)
    }

    override fun getUploadServerUrl(): Single<UploadServerUrl> {
        return Single.create { emitter ->
            VK.execute(
                VKGetUploadServerUrlRequest(),
                object : VKApiCallback<UploadServerUrlResponse> {
                    override fun success(result: UploadServerUrlResponse) {
                        val mappedResult = UploadServerUrlConverter.fromApiToUI(result)
                        emitter.onSuccess(mappedResult)
                    }

                    override fun fail(error: VKApiExecutionException) {
                        emitter.onError(error)
                    }
                })
        }
    }

    override fun uploadPhoto(photo: File, uploadUrl: String): Single<PhotoUploading> {
        return Single.create { emitter ->
            val filePart = MultipartBody.Part.createFormData(
                "file", photo.name, RequestBody.create(
                    MediaType.parse("image/*"), photo
                )
            )

            val call = api.uploadPhoto(filePart, uploadUrl)
            val response = call.execute()
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val mappedResult = PhotoUploadingConverter.fromApiToUI(responseBody)
                emitter.onSuccess(mappedResult)
            } else {
                emitter.onError(IllegalStateException("Error while uploading photo"))
            }
        }
    }

    override fun saveWallPhoto(photoUploading: PhotoUploading): Single<SaveWallPhoto> {
        return Single.create { emitter ->
            VK.execute(
                VKSaveWallPhotoRequest(
                    photoUploading.server,
                    photoUploading.photo,
                    photoUploading.hash
                ),
                object : VKApiCallback<SaveWallPhotoResponse> {
                    override fun success(result: SaveWallPhotoResponse) {
                        val mappedResult = SaveWallPhotoConverter.fromApiToUI(result)
                        emitter.onSuccess(mappedResult)
                    }

                    override fun fail(error: VKApiExecutionException) {
                        emitter.onError(error)
                    }
                })
        }
    }

    override fun publishWallPost(saveWallPhoto: SaveWallPhoto, message: String): Single<Int> {
        return Single.create { emitter ->
            VK.execute(
                VKPublishWallPostRequest(saveWallPhoto.ownerId, saveWallPhoto.photoId, message),
                object : VKApiCallback<Int> {
                    override fun success(result: Int) {
                        emitter.onSuccess(result)
                    }

                    override fun fail(error: VKApiExecutionException) {
                        emitter.onError(error)
                    }
                })
        }
    }

    interface VKService {
        @Multipart
        @POST
        fun uploadPhoto(@Part photo: MultipartBody.Part, @Url url: String): Call<PhotoUploadingResponse>
    }
}