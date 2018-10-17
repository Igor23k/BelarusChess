package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

/**
 * Created by Igor on 05.04.2018.
 */

class UserDTO : Serializable {

    var id: Long? = null

    var name: String? = null

    var surname: String? = null

    var patronymic: String? = null

    var birthday: String? = null

    var email: String? = null

    var status: String? = null

    var phoneNumber: String? = null

    var password: String? = null

    var beCoach: Boolean = false

    var beOrganizer: Boolean = false

    var beMale: Boolean = false

    var rank: RankDTO? = null

    var country: CountryDTO? = null

    var rating: Int? = null

    var coach: UserDTO? = null

    var places: List<PlaceDTO>? = null

    constructor(id: Long?, name: String?, surname: String?, patronymic: String?, birthday: String?, email: String?, status: String?, phoneNumber: String?, password: String?, beCoach: Boolean, beOrganizer: Boolean, beMale: Boolean, rank: RankDTO?, country: CountryDTO?, rating: Int?, coach: UserDTO?, places: List<PlaceDTO>?) {
        this.id = id
        this.name = name
        this.surname = surname
        this.patronymic = patronymic
        this.birthday = birthday
        this.email = email
        this.status = status
        this.phoneNumber = phoneNumber
        this.password = password
        this.beCoach = beCoach
        this.beOrganizer = beOrganizer
        this.beMale = beMale
        this.rank = rank
        this.country = country
        this.rating = rating
        this.coach = coach
        this.places = places
    }

    constructor() {}
}