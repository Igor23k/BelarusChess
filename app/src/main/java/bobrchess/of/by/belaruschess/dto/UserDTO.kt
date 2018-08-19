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

    var rank: RankDTO? = null

    var country: CountryDTO? = null

    var rating: Int? = null

    var coach: UserDTO? = null

    constructor(id: Long?, name: String, surname: String, patronymic: String, birthdate: String, email: String, status: String, phoneNumber: String, rating: Int?, password: String, rank: RankDTO, country: CountryDTO, coach: UserDTO) {
        this.id = id
        this.name = name
        this.surname = surname
        this.patronymic = patronymic
        this.birthday = birthdate
        this.email = email
        this.status = status
        this.phoneNumber = phoneNumber
        this.password = password
        this.rank = rank
        this.country = country
        this.rating = rating
        this.coach = coach
    }

    constructor() {}
}