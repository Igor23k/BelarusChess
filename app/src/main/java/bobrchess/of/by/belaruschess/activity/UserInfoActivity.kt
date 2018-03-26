package bobrchess.of.by.belaruschess.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.colibritweet.adapter.TweetAdapter
import bobrchess.of.by.colibritweet.pojo.Tweet
import bobrchess.of.by.colibritweet.pojo.User
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by Igor on 25.03.2018.
 */
class UserInfoActivity : AppCompatActivity() {
    companion object {
        val USER_ID = "userId"
    }

    private var userImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var nickTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var locationTextView: TextView? = null
    private var followingCountTextView: TextView? = null
    private var followersCountTextView: TextView? = null
    private var tweetsRecyclerView: RecyclerView? = null
    private var tweetAdapter: TweetAdapter? = null
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        userImageView = findViewById(R.id.user_image_view)
        nameTextView = findViewById(R.id.user_name_text_view)
        nickTextView = findViewById(R.id.user_nick_text_view)
        descriptionTextView = findViewById(R.id.user_description_text_view)
        locationTextView = findViewById(R.id.user_location_text_view)
        followingCountTextView = findViewById(R.id.following_count_text_view)
        followersCountTextView = findViewById(R.id.followers_count_text_view)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        loadUserInfo()
        initRecyclerView()
        loadTweets()
    }

    private fun loadTweets() {
        val tweets = getTweets()
        tweetAdapter!!.setItems(tweets)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_search) {
            val intent = Intent(this, SearchUsersActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    private fun getTweets(): Collection<Tweet> {
        return Arrays.asList(
                Tweet(getUser(), 1L, "Thu Dec 13 07:31:08 +0000 2017", "Очень длинное описание твита 1",
                        4L, 4L, "https://www.w3schools.com/w3css/img_fjords.jpg"),

                Tweet(getUser(), 2L, "Thu Dec 12 07:31:08 +0000 2017", "Очень длинное описание твита 2",
                        5L, 5L, "https://www.w3schools.com/w3images/lights.jpg"),

                Tweet(getUser(), 3L, "Thu Dec 11 07:31:08 +0000 2017", "Очень длинное описание твита 3",
                        6L, 6L, "https://www.w3schools.com/css/img_mountains.jpg")
        )
    }

    private fun initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view)
        tweetsRecyclerView!!.layoutManager = LinearLayoutManager(this)
        tweetAdapter = TweetAdapter()
        tweetsRecyclerView!!.adapter = tweetAdapter
    }

    private fun loadUserInfo() {
        val user = getUser()
        displayUserInfo(user)
    }

    private fun displayUserInfo(user: User) {
        Picasso.with(this).load(user.imageUrl).into(userImageView)
        nameTextView!!.text = user.name
        nickTextView!!.text = user.nick
        descriptionTextView!!.text = user.description
        locationTextView!!.text = user.location

        val followingCount = user.followingCount.toString()
        followingCountTextView!!.text = followingCount

        val followersCount = user.followersCount.toString()
        followersCountTextView!!.text = followersCount

        supportActionBar!!.title = user.name
    }


    private fun getUser(): User {
        return User(
                1L,
                "http://priscree.ru/img/f6334f93a90da8.jpg",
                "Igor",
                "Igor23k",
                "Sample description",
                "Belarus",
                42,
                42
        )
    }
}