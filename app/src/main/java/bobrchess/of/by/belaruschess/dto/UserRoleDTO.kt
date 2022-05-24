package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

class UserRoleDTO : Serializable {

    var id: Int? = null
    var role: RoleDTO? = null

    constructor(id: Int?, role: RoleDTO?) {
        this.id = id
        this.role = role
    }

    constructor()

}
