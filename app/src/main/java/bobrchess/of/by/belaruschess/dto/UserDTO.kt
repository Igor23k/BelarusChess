package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

open class UserDTO : Serializable {

    var id: Long? = null
    var name: String? = null
    var surname: String? = null
    var patronymic: String? = null
    var birthday: String? = null
    var email: String? = null
    var password: String? = null
    var beCoach: Boolean = false
    var beAdmin: Boolean = false
    var beOrganizer: Boolean = false
    var beMale: Boolean? = null
    var rank: RankDTO? = null
    var country: CountryDTO? = null
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
        this.beCoach = userDTO.beCoach
        this.beAdmin = userDTO.beAdmin
        this.beOrganizer = userDTO.beOrganizer
        this.beMale = userDTO.beMale
        this.rank = userDTO.rank
        this.country = userDTO.country
        this.rating = userDTO.rating
        this.coach = userDTO.coach
        this.places = userDTO.places
        this.image = userDTO.image
    }

    constructor(id: Long?, name: String?, surname: String?, patronymic: String?, birthday: String?,
                email: String?, password: String?,
                beCoach: Boolean, beOrganizer: Boolean, beAdmin: Boolean, beMale: Boolean?, rank: RankDTO?,
                country: CountryDTO?, rating: Int?, coach: String?,
                places: List<PlaceDTO>?, image: ByteArray) {
        this.id = id
        this.name = name
        this.surname = surname
        this.patronymic = patronymic
        this.birthday = birthday
        this.email = email
        this.password = password
        this.beCoach = beCoach
        this.beOrganizer = beOrganizer
        this.beAdmin = beAdmin
        this.beMale = beMale
        this.rank = rank
        this.country = country
        this.rating = rating
        this.coach = coach
        this.places = places
        this.image = image
    }

    constructor()
}