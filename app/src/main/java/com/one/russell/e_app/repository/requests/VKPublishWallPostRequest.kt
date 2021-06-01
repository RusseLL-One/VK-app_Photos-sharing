package com.one.russell.e_app.repository.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONException
import org.json.JSONObject

class VKPublishWallPostRequest(ownerId: Int, photoId: Int, message: String) :
    VKRequest<Int>("wall.post") {

    init {
        addParam("attachments", "photo$ownerId" + "_$photoId")
        addParam("message", message)
    }

    override fun parse(r: JSONObject): Int {
        val response = r.getJSONObject("response")
        try {
            return response.getInt("post_id")
        } catch (e: JSONException) {
            throw e
        }
    }
}