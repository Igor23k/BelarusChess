package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

open class PlaceDTO : Serializable {

    var id: Int? = null
    var name: String? = null
    var city: String? = null
    var street: String? = null
    var building: String? = null
    var capacity: Int? = null
    var country: CountryDTO? = null
    var approved: Boolean = false
    var createdBy: UserDTO? = null
    var image: String? = null

    constructor(placeDTO: PlaceDTO) {
        this.id = placeDTO.id
        this.name = placeDTO.name
        this.city = placeDTO.city
        this.street = placeDTO.street
        this.building = placeDTO.building
        this.capacity = placeDTO.capacity
        this.country = placeDTO.country
        this.approved = placeDTO.approved
        this.createdBy = placeDTO.createdBy
        this.image = placeDTO.image
    }

    constructor(id: Int?, name: String, city: String, street: String, building: String, capacity: Int?,
                country: CountryDTO, approved: Boolean, createdBy: UserDTO, image: String) {
        this.id = id
        this.name = name
        this.city = city
        this.street = street
        this.building = building
        this.capacity = capacity
        this.country = country
        this.approved = approved
        this.createdBy = createdBy
        this.image = image
    }

    constructor()
}
