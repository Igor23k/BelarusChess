package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.util.Constants
import java.text.DateFormat
import java.util.*

class EventUser(
        var id: Int,
        var birthday: Date,
        var name: String,
        var surname: String) :
        EventDate(birthday, id) {

    /**
     * Identifier is an identifier for sorting
     */
    enum class Identifier : SortIdentifier {
        Birthday {
            override fun Identifier(): Int = 0
        },
        Name {
            override fun Identifier(): Int = 1
        },
        Surname {
            override fun Identifier(): Int = 2
        },
        Patronymic {
            override fun Identifier(): Int = 3
        },
        Id {
            override fun Identifier(): Int = 4
        },
        ImageUri {
            override fun Identifier(): Int = 5
        },
        Rating {
            override fun Identifier(): Int = 6
        },
        RankId {
            override fun Identifier(): Int = 7
        },
        CountryId {
            override fun Identifier(): Int = 8
        },
        Coach {
            override fun Identifier(): Int = 9
        },
        Email {
            override fun Identifier(): Int = 10
        },
        PhoneNumber {
            override fun Identifier(): Int = 11
        },
        BeCoach {
            override fun Identifier(): Int = 12
        },
        BeAdmin {
            override fun Identifier(): Int = 13
        },
        BeOrganizer {
            override fun Identifier(): Int = 14
        },
        BeMale {
            override fun Identifier(): Int = 15
        }/*,
        Places {
            override fun Identifier(): Int = 16
        }*/
    }

    var patronymic: String? = null
    var imageUri: String? = null
    var rating: Int? = null
    var rankId: Int? = null
    var countryId: Int? = null
    var coach: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var beCoach: Boolean? = null
    var beAdmin: Boolean? = null
    var beOrganizer: Boolean? = null
    var beMale: Boolean? = null
    //var places: Long? = null

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.Birthday}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}${IOHandler.tournamentDivider_properties}" +
                getStringFromValue(Identifier.Surname, this.surname) +
                getStringFromValue(Identifier.Patronymic, this.patronymic) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.ImageUri, this.imageUri) +
                getStringFromValue(Identifier.Rating, this.rating) +
                getStringFromValue(Identifier.RankId, this.rankId) +
                getStringFromValue(Identifier.CountryId, this.countryId) +
                getStringFromValue(Identifier.Coach, this.coach) +
                getStringFromValue(Identifier.PhoneNumber, this.phoneNumber) +
                getStringFromValue(Identifier.BeCoach, this.beCoach) +
                getStringFromValue(Identifier.BeAdmin, this.beAdmin) +
                getStringFromValue(Identifier.BeOrganizer, this.beOrganizer) +
                getStringFromValue(Identifier.Email, this.email) +
                getStringFromValue(Identifier.BeMale, this.beMale)
    }

    companion object {
        const val Name: String = "EventUser"
    }
}