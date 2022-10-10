package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import android.util.Log
import com.codepath.apps.restclienttemplate.TimeFormatter
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
data class Tweet(
    var tweetBody: String,
    var createdAt: String,
    var mediaUrl: String,
    var user: User?= null
) : Parcelable {

    companion object{
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet>{
            val tweets = mutableListOf<Tweet>()
            var url: String = ""
            for(i in 0 until jsonArray.length()){
                val tweetObject = jsonArray.getJSONObject(i)
                val entitiesObject = tweetObject.getJSONObject("entities")
                if (entitiesObject.has("media")){
                    val mediaObject:JSONObject = entitiesObject.getJSONArray("media")[0] as JSONObject
                    Log.i("Media Object", "$mediaObject")
                    url = mediaObject.getString("media_url_https")
                    Log.i("url", "$url")
                }
                tweets.add(
                    Tweet(
                        tweetObject.getString("text"),
                        tweetObject.getString("created_at"),
                        url,
                        User.fromJson(tweetObject.getJSONObject("user"))
                    )
                )
                url = ""
            }
            return tweets
        }
        // time parameter is "created_at" data from a single tweet
        fun getFormattedTimeStamp(time: String): String? {
            return TimeFormatter.getTimeDifference(time)
        }
    }
}