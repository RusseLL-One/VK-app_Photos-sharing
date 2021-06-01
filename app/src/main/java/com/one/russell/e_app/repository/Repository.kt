package com.one.russell.e_app.repository

import android.content.Context
import com.one.russell.e_app.data.PhotoUploading
import com.one.russell.e_app.data.SaveWallPhoto
import com.one.russell.e_app.data.UploadServerUrl
import com.vk.api.sdk.auth.VKAccessToken
import io.reactivex.Single
import java.io.File

interface Repository {

    fun getUploadServerUrl(): Single<UploadServerUrl>

    fun uploadPhoto(photo: File, uploadUrl: String): Single<PhotoUploading>

    fun saveWallPhoto(photoUploading: PhotoUploading): Single<SaveWallPhoto>

    fun publishWallPost(saveWallPhoto: SaveWallPhoto, message: String): Single<Int>

    fun saveAccessToken(context: Context, accessToken: VKAccessToken)

    fun getAccessToken(context: Context): VKAccessToken?
}