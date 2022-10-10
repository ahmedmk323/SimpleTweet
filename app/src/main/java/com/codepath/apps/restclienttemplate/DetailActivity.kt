package com.codepath.apps.restclienttemplate

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.apps.restclienttemplate.models.User
import java.time.LocalDateTime.parse
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class DetailActivity : AppCompatActivity() {
    lateinit var avatar: ImageView
    lateinit var screen_name: TextView
    lateinit var user_name: TextView
    lateinit var tweetText: TextView
    lateinit var createdAt: TextView
    lateinit var mediaCanvas: ImageView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        avatar = findViewById(R.id.ivProfilePic)
        screen_name = findViewById(R.id.tvSname)
        user_name = findViewById(R.id.tvName)
        tweetText = findViewById(R.id.tvBody)
        createdAt = findViewById(R.id.created_at)
        mediaCanvas = findViewById(R.id.ivMedia)

        val tweet = intent.getParcelableExtra<Tweet>("Tweet")
        val user = intent.getParcelableExtra<User>("User")
        tweetText.text = tweet?.tweetBody
        screen_name.text = "@${user?.screenName}"
        user_name.text = user?.name
        if (tweet != null) {
            createdAt.text = timeConverter(tweet.createdAt)
        }
        Log.i("Tweet", "$tweet")
        if (tweet?.mediaUrl != ""){
            Glide.with(this)
                .load(tweet?.mediaUrl)
                .transform(RoundedCorners(25))
                .into(mediaCanvas)
        } else { mediaCanvas.visibility= View.GONE}

        Glide.with(this)
            .load(user?.publicImageUrl)
            .transform(CircleCrop())
            .into(avatar)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun timeConverter (s: String): String {
        var dateTime = parse(s, DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss ZZ y"))

        val zonedDateTime = dateTime.atZone(ZoneId.of("America/New_York"))

        return  zonedDateTime.format(DateTimeFormatter.ofPattern("KK:mm a '.' MMM dd, y"))
    }
}