package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

/**
 * Created by Igor on 10.04.2018.
 */

class TeamDTO : Serializable {
    var id: Int? = null

    var name: String? = null

    var players: List<UserDTO>? = null

    constructor(id: Int?, name: String, players: List<UserDTO>) {
        this.id = id
        this.name = name
        this.players = players
    }

    constructor() {}
}
