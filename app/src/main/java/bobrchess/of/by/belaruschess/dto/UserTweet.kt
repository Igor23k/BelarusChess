package bobrchess.of.by.colibritweet.pojo

/**
 * Created by Igor on 14.03.2018.
 */
class UserTweet {
    var id: Long? = null
    var imageUrl: String? = null
    var name: String? = null
    var nick: String? = null
    var description: String? = null
    var location: String? = null
    var followingCount: Int? = null
    var followersCount: Int? = null

    constructor(id: Long?, imageUrl: String?, name: String?, nick: String?, description: String?, location: String?, followingCount: Int?, followersCount: Int?) {
        this.id = id
        this.imageUrl = imageUrl
        this.name = name
        this.nick = nick
        this.description = description
        this.location = location
        this.followingCount = followingCount
        this.followersCount = followersCount
    }

}