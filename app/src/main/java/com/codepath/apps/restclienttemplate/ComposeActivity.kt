package com.codepath.apps.restclienttemplate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tvCount: TextView
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btTweet)
        tvCount = findViewById<TextView>(R.id.tvCount)

        //Textwatcher to track the number of characters inputted
        etCompose.addTextChangedListener(object : TextWatcher{
            @SuppressLint("ResourceType")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                tvCount.setTextColor(Color.GRAY)
            }

            @SuppressLint("ResourceType")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! == 280){
                    tvCount.setTextColor(Color.RED)
                }
                tvCount.text = s?.length.toString()

            }

            override fun afterTextChanged(s: Editable?) {
                btnTweet.isEnabled = !(s?.length!! > 280 || s?.length!! == 0)
            }

        })

        client = TwitterApplication.getRestClient(this)

        // Handling the user's click on the tweet button

        btnTweet.setOnClickListener {

            //Grab the content of the edit text
            val tweetContent = etCompose.text.toString()

            // 1. Tweet must not be empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this,"Empty tweets not allowed", Toast.LENGTH_SHORT).show()
            }
            // 2. Tweet must be under character count
            else if (tweetContent.length > 280){
                Toast.makeText(this,"Tweet is greater than 280 characters ${tweetContent.length}!!", Toast.LENGTH_SHORT).show()
            }else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG,"Successfullt published Tweet")

                        val tweet = Tweet.fromJsonArray(json.jsonArray) as ArrayList<Tweet>

                        val intent = Intent()
                        intent.putExtra("tweet",tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        TODO("Not yet implemented")
                    }


                })
            }
        // Make an api call to twitter
        }

    }
    companion object{
        val TAG = "ComposeActivity"
    }
}