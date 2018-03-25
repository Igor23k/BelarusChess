package bobrchess.of.by.colibritweet.pojo

/**
 * Created by Igor on 14.03.2018.
 */
data class Tweet(var user: User, var id: Long, var creationDate: String, var text: String,
                 var retweetCount: Long, var favoriteCount: Long, var imageUrl: String?)