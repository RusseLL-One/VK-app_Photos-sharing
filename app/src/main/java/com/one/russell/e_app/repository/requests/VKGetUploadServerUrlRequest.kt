package com.one.russell.e_app.repository.requests

import com.one.russell.e_app.repository.entities.UploadServerUrlResponse
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKGetUploadServerUrlRequest() :
    VKRequest<UploadServerUrlResponse>("photos.getWallUploadServer") {

    override fun parse(r: JSONObject): UploadServerUrlResponse {
        val response = r.getJSONObject("response")
        return UploadServerUrlResponse.parse(response)
    }
}