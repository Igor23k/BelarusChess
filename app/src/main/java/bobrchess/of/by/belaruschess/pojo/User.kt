package bobrchess.of.by.colibritweet.pojo

/**
 * Created by Igor on 14.03.2018.
 */
data class User(var id: Long, var imageUrl: String, var name: String,
                var nick: String, var description: String, var location:
                String, var followingCount: Int, var followersCount: Int)