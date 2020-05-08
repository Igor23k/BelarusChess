package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import java.util.*
class EventPlace(
        var id: Int,
        var name: String) :
        EventDate(Date("12/12/2014"), id) {//todo

    /**
     * Identifier is an identifier for sorting
     */
    enum class Identifier : SortIdentifier {
        Name {
            override fun Identifier(): Int = 2
        },
        City {
            override fun Identifier(): Int = 3
        },
        Street {
            override fun Identifier(): Int = 4
        },
        Id {
            override fun Identifier(): Int = 5
        },
        ImageUri {
            override fun Identifier(): Int = 6
        },
        Building {
            override fun Identifier(): Int = 7
        },
        Capacity {
            override fun Identifier(): Int = 8
        },
        Country {
            override fun Identifier(): Int = 9
        },
        Approved {
            override fun Identifier(): Int = 10
        }
    }

    var capacity: Int? = null

    var city: String? = null

    var street: String? = null

    var building: String? = null

    var countryId: Int? = null

    var approved: Boolean? = null

    var imageUri: String? = null

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                getStringFromValue(Identifier.Name, this.name) +
                getStringFromValue(Identifier.City, this.city) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.Street, this.street) +
                getStringFromValue(Identifier.Country, this.countryId) +
                getStringFromValue(Identifier.Building, this.building) +
                getStringFromValue(Identifier.Capacity, this.capacity) +
                getStringFromValue(Identifier.Approved, this.approved) +
                getStringFromValue(Identifier.ImageUri, this.imageUri)
    }

    companion object {
        const val Name: String = "EventTournament"
    }
}