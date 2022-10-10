package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient
    lateinit var adapter: TweetsAdapter
    lateinit var rvTweets: RecyclerView
    lateinit var swipeContainer: SwipeRefreshLayout
    val tweets = mutableListOf<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        swipeContainer = findViewById(R.id.swipeContainer)
        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(this, tweets as ArrayList<Tweet>)
        rvTweets.adapter = adapter
        rvTweets.layoutManager = LinearLayoutManager(this)
        client = TwitterApplication.getRestClient(this)
        swipeContainer.setOnRefreshListener {
            populateHomeTimeline()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
        populateHomeTimeline()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    // Handles clicks on menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.compose){
            val intent = Intent(this, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){

            //Get data from our Intent
            val tweet = data?.getParcelableExtra("tweet") as Tweet?

            //update timeline

            //modifying the data source of tweets
            tweets.add(0,tweet!!)

            //update adapter
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun populateHomeTimeline() {
        client.getHomeTimeline(object: JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess!! $json")
                val jsonArray= json.jsonArray
                try {
                    // Clear out current fetched tweets
                    adapter.clear()
                    val newTweets = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(newTweets)

                    adapter.notifyDataSetChanged()
                    swipeContainer.setRefreshing(false)
                } catch (e: JSONException){
                    Log.e("Error", "$e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure!!")
            }

        })
    }
    companion object {
        val TAG= "TimelineActivity"
        const val REQUEST_CODE = 20
    }
}