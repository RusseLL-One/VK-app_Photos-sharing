package com.one.russell.e_app.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.russell.e_app.repository.Repository
import com.one.russell.e_app.repository.RepositoryImpl
import com.vk.api.sdk.auth.VKAccessToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.content.ContentResolver
import java.io.*


class MainViewModel : ViewModel() {
    private val repository: Repository = RepositoryImpl()
    private var uploadingPostLiveData = MutableLiveData<Int>()

    private var disposable: Disposable? = null

    fun publishPost(photo: File, message: String) {
        disposable = repository.getUploadServerUrl()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { urlResponse ->
                repository.uploadPhoto(photo, urlResponse.uploadUrl)
            }.flatMap { uploadingResponse ->
                repository.saveWallPhoto(uploadingResponse)
            }.flatMap { savingResponse ->
                repository.publishWallPost(savingResponse, message)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ postId ->
                uploadingPostLiveData.value = postId
            }, { throwable ->
                uploadingPostLiveData.value = -1
                Log.e("publishPost", throwable.localizedMessage ?: "ERROR")
            })
    }

    fun getUploadingPostLiveData(): LiveData<Int> {
        return uploadingPostLiveData
    }

    fun saveImage(photoUri: Uri?, contentResolver: ContentResolver): File? {
        if (photoUri == null) return null

        val inputStream = contentResolver.openInputStream(photoUri)
        val tempFile = File.createTempFile("tmp_file", ".jpg")
        FileOutputStream(tempFile).use { output ->
            inputStream?.copyTo(output)
            output.flush()
        }

        return tempFile
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun saveAccessToken(context: Context, accessToken: VKAccessToken) {
        repository.saveAccessToken(context, accessToken)
    }

    fun getAccessToken(context: Context): VKAccessToken? {
        return repository.getAccessToken(context)
    }
}