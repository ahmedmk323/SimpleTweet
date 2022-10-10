package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User (
    var name:String,
    var screenName: String,
    var publicImageUrl: String
    ) : Parcelable {
    companion object{
        fun fromJson(jsonObject: JSONObject): User {
            return User(
                jsonObject.getString("name"),
                jsonObject.getString("screen_name"),
                jsonObject.getString("profile_image_url_https").replace("_normal","") //substituted image dimension from 48x48 pixels to "original" dimension
            )
        }
    }
}