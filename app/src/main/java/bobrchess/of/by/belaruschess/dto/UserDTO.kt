package bobrchess.of.by.belaruschess.dto

import bobrchess.of.by.belaruschess.util.Constants
import java.io.Serializable

open class UserDTO : Serializable {

    var id: Long? = null
    var name: String? = null
    var surname: String? = null
    var patronymic: String? = null
    var birthday: String? = null
    var email: String? = null
    var password: String? = null
    var beMale: Boolean? = null
    var rank: RankDTO? = null
    var country: CountryDTO? = null
    var roles: List<UserRoleDTO>? = null
    var rating: Int? = null
    var coach: String? = null
    var places: List<PlaceDTO>? = null
    var image: ByteArray? = null

    constructor(userDTO: UserDTO) {
        this.id = userDTO.id
        this.name = userDTO.name
        this.surname = userDTO.surname
        this.patronymic = userDTO.patronymic
        this.birthday = userDTO.birthday
        this.email = userDTO.email
        this.password = userDTO.password
        this.beMale = userDTO.beMale
        this.rank = userDTO.rank
        this.country = userDTO.country
        this.roles = userDTO.roles
        this.rating = userDTO.rating
        this.coach = userDTO.coach
        this.places = userDTO.places
        this.image = userDTO.image
    }

    constructor(id: Long?, name: String?, surname: String?, patronymic: String?, birthday: String?,
                email: String?, password: String?, beMale: Boolean?, rank: RankDTO?, country: CountryDTO?,
                roles: List<UserRoleDTO>?, rating: Int?, coach: String?, places: List<PlaceDTO>?, image: ByteArray) {
        this.id = id
        this.name = name
        this.surname = surname
        this.patronymic = patronymic
        this.birthday = birthday
        this.email = email
        this.password = password
        this.beMale = beMale
        this.rank = rank
        this.country = country
        this.roles = roles
        this.rating = rating
        this.coach = coach
        this.places = places
        this.image = image
    }

    constructor()

    fun isAdmin(): Boolean {
        return roles?.stream()?.filter { userRole ->userRole.role?.id!! == Constants.ROLE_ADMIN_ID }?.findFirst()?.isPresent == true
    }

    fun isCoach(): Boolean {
        return roles?.stream()?.filter { userRole ->userRole.role?.id!! == Constants.ROLE_COACH_ID }?.findFirst()?.isPresent == true
    }

    fun isOrganizer(): Boolean {
        return roles?.stream()?.filter { userRole ->userRole.role?.id!! == Constants.ROLE_ORGANIZER_ID }?.findFirst()?.isPresent == true
    }
}