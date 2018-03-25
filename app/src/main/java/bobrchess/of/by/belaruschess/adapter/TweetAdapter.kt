package bobrchess.of.by.colibritweet.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.colibritweet.pojo.Tweet
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


// Унаследовали наш адаптер от RecyclerView.Adapter
// Здесь же указали наш собственный ViewHolder, который предоставит нам доступ к View-компонентам
class TweetAdapter : RecyclerView.Adapter<TweetAdapter.TweetViewHolder>() {

    private val tweetList = ArrayList<Tweet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item_view, parent, false)
        return TweetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(tweetList[position])
    }

    override fun getItemCount(): Int {
        return tweetList.size
    }

    fun setItems(tweets: Collection<Tweet>) {
        tweetList.addAll(tweets)
        notifyDataSetChanged()
    }

    fun clearItems() {
        tweetList.clear()
        notifyDataSetChanged()
    }

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    inner class TweetViewHolder// Мы также создали конструктор, который принимает на вход View-компонент строкИ
    // и ищет все дочерние компоненты
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private val userImageView: ImageView
        private val nameTextView: TextView
        private val nickTextView: TextView
        private val creationDateTextView: TextView
        private val contentTextView: TextView
        private val tweetImageView: ImageView
        private val retweetsTextView: TextView
        private val likesTextView: TextView

        init {
            userImageView = itemView.findViewById(R.id.profile_image_view)
            nameTextView = itemView.findViewById(R.id.author_name_text_view)
            nickTextView = itemView.findViewById(R.id.author_nick_text_view)
            creationDateTextView = itemView.findViewById(R.id.creation_date_text_view)
            contentTextView = itemView.findViewById(R.id.tweet_content_text_view)
            tweetImageView = itemView.findViewById(R.id.tweet_image_view)
            retweetsTextView = itemView.findViewById(R.id.retweets_text_view)
            likesTextView = itemView.findViewById(R.id.likes_text_view)
        }

        fun bind(tweet: Tweet) {
            nameTextView.text = tweet.user.name
            nickTextView.text = tweet.user.nick
            contentTextView.text = tweet.text
            retweetsTextView.text = tweet.retweetCount.toString()
            likesTextView.text = tweet.favoriteCount.toString()

            val creationDateFormatted = getFormattedDate(tweet.creationDate)
            creationDateTextView.text = creationDateFormatted

            Picasso.with(itemView.context).load(tweet.user.imageUrl).into(userImageView)

            val tweetPhotoUrl = tweet.imageUrl
            Picasso.with(itemView.context).load(tweetPhotoUrl).into(tweetImageView)

            tweetImageView.visibility = if (tweetPhotoUrl != null) View.VISIBLE else View.GONE
        }

        private fun getFormattedDate(rawDate: String): String {
            val utcFormat = SimpleDateFormat(TWITTER_RESPONSE_FORMAT, Locale.ROOT)
            val displayedFormat = SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault())
            try {
                val date = utcFormat.parse(rawDate)
                return displayedFormat.format(date)
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }

        }
    }

    companion object {

        private val TWITTER_RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy" // Thu Oct 26 07:31:08 +0000 2017
        private val MONTH_DAY_FORMAT = "MMM d" // Oct 26
    }
}