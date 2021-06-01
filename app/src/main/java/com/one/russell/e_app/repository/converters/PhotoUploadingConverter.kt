package com.one.russell.e_app.repository.converters

import com.one.russell.e_app.data.PhotoUploading
import com.one.russell.e_app.repository.entities.PhotoUploadingResponse

object PhotoUploadingConverter {

    fun fromApiToUI(photoUploadingResponse: PhotoUploadingResponse): PhotoUploading {
        return PhotoUploading(
            photoUploadingResponse.server,
            photoUploadingResponse.photo,
            photoUploadingResponse.hash
        )
    }
}