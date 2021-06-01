package com.one.russell.e_app.repository.requests

import com.one.russell.e_app.repository.entities.SaveWallPhotoResponse
import com.one.russell.e_app.repository.entities.UploadServerUrlResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKSaveWallPhotoRequest(server: Int, photo: String, hash: String) :
    VKRequest<SaveWallPhotoResponse>("photos.saveWallPhoto") {

    init {
        addParam("server", server)
        addParam("photo", photo)
        addParam("hash", hash)
    }

    override fun parse(r: JSONObject): SaveWallPhotoResponse {
        val response = r.getJSONArray("response").getJSONObject(0)
        return SaveWallPhotoResponse.parse(response)
    }
}