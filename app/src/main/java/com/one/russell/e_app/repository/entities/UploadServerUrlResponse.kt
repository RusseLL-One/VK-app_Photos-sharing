package com.one.russell.e_app.repository.entities

import org.json.JSONException
import org.json.JSONObject

data class UploadServerUrlResponse(var uploadUrl: String, var albumId: Int, var userId: Int) {

    companion object {
        fun parse(responseBody: JSONObject): UploadServerUrlResponse {
            try {
                val uploadUrl = responseBody.getString("upload_url")
                val albumId = responseBody.getInt("album_id")
                val userId = responseBody.getInt("user_id")
                return UploadServerUrlResponse(
                    uploadUrl,
                    albumId,
                    userId
                )
            } catch (e: JSONException) {
                throw e
            }
        }
    }
}