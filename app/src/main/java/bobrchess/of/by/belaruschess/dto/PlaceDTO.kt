package bobrchess.of.by.belaruschess.dto

/**
 * Created by Igor on 10.04.2018.
 */

class PlaceDTO {
    var id: Int? = null

    var name: String? = null

    var city: String? = null

    var street: String? = null

    var building: String? = null

    var capacity: Int? = null

    var country: CountryDTO? = null

    constructor(id: Int?, name: String, city: String, street: String, building: String, capacity: Int?, country: CountryDTO) {
        this.id = id
        this.name = name
        this.city = city
        this.street = street
        this.building = building
        this.capacity = capacity
        this.country = country
    }

    constructor() {}
}
