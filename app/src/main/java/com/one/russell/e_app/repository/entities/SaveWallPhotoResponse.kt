package com.one.russell.e_app.repository.entities

import org.json.JSONException
import org.json.JSONObject

data class SaveWallPhotoResponse(var photoId: Int, var ownerId: Int) {

    companion object {
        fun parse(responseBody: JSONObject): SaveWallPhotoResponse {
            try {
                val id = responseBody.getInt("id")
                val ownerId = responseBody.getInt("owner_id")
                return SaveWallPhotoResponse(
                    id,
                    ownerId
                )
            } catch (e: JSONException) {
                throw e
            }
        }
    }
}