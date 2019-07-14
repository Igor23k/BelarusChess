package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

/**
 * Created by Igor on 10.04.2018.
 */

class TokenDTO : Serializable{
    var token: String? = null

    constructor(name: String?) {
        this.token = name
    }

    constructor()
}
