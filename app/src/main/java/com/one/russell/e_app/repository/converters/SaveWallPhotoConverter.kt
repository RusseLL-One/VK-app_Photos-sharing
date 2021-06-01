package com.one.russell.e_app.repository.converters

import com.one.russell.e_app.data.SaveWallPhoto
import com.one.russell.e_app.repository.entities.SaveWallPhotoResponse

object SaveWallPhotoConverter {

    fun fromApiToUI(saveWallPhotoResponse: SaveWallPhotoResponse): SaveWallPhoto {
        return SaveWallPhoto(
            saveWallPhotoResponse.photoId,
            saveWallPhotoResponse.ownerId
        )
    }
}