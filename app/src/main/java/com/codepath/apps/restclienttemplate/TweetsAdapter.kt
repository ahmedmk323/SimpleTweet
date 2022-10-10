package com.codepath.apps.restclienttemplate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(private val context:Context, private val tweets:ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>(){
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val username = itemView.findViewById<TextView>(R.id.tvUsername)
        private val tweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        private val relativeTime = itemView.findViewById<TextView>(R.id.tvRtime)
        private val avatar = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(tweet: Tweet) {
            username.text = tweet.user?.name
            tweetBody.text=tweet.tweetBody
            relativeTime.text = Tweet.getFormattedTimeStamp(tweet.createdAt)
            Glide.with(context)
                .load(tweet.user?.publicImageUrl)
                .transform(CircleCrop())
                .into(avatar)
        }

        override fun onClick(v: View?) {
            val tweet = tweets[adapterPosition]

            val i = Intent(context,DetailActivity::class.java)
            i.putExtra("Tweet",tweet)
            i.putExtra("User",tweet.user)
            context.startActivity(i)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context

        //Inflate item layout
        val view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = tweets[position]
        return holder.bind(tweet)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }
    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }
}