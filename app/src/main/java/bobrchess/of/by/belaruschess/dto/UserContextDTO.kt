package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

/**
 * Created by Igor on 05.04.2018.
 */

class UserContextDTO : Serializable {

    var user: UserDTO
    var tokenMap: Map<String, String>

    constructor(user: UserDTO, tokenMap: Map<String, String>) {
        this.user = user
        this.tokenMap = tokenMap
    }
}