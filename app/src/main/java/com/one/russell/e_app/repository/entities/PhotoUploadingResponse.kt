package com.one.russell.e_app.repository.entities

import org.json.JSONException
import org.json.JSONObject

class PhotoUploadingResponse(var server: Int, var photo: String, var hash: String) {

    companion object {
        fun parse(responseBody: JSONObject): PhotoUploadingResponse {
            try {
                val server = responseBody.getInt("server")
                val photo = responseBody.getString("photo")
                val hash = responseBody.getString("hash")
                return PhotoUploadingResponse(server, photo, hash)
            } catch (e: JSONException) {
                throw e
            }
        }
    }
}