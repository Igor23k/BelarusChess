package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

class TokenDTO : Serializable {

    var token: String? = null

    constructor(name: String?) {
        this.token = name
    }

    constructor()
}
