package com.one.russell.e_app.repository.converters

import com.one.russell.e_app.data.UploadServerUrl
import com.one.russell.e_app.repository.entities.UploadServerUrlResponse

object UploadServerUrlConverter {

    fun fromApiToUI(uploadServerUrlResponse: UploadServerUrlResponse): UploadServerUrl {
        return UploadServerUrl(
            uploadServerUrlResponse.uploadUrl,
            uploadServerUrlResponse.albumId,
            uploadServerUrlResponse.userId
        )
    }
}