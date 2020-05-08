package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
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
        AvatarUri {
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
        CoachId {
            override fun Identifier(): Int = 9
        }
    }


    var patronymic: String? = null
    var imageUri: String? = null
    var rating: Int? = null
    var rankId: Int? = null
    var countryId: Int? = null
    var coachId: Long? = null


    /**
     * toString returns EventTournament as string representation
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE|AVATARURI|NICKNAME
     * @return String
     */
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
                getStringFromValue(Identifier.AvatarUri, this.imageUri) +
                getStringFromValue(Identifier.Rating, this.rating) +
                getStringFromValue(Identifier.RankId, this.rankId) +
                getStringFromValue(Identifier.CountryId, this.countryId) +
                getStringFromValue(Identifier.CoachId, this.coachId)
    }

    /**
     * toString returns EventTournament as string representation but excludes a possible avatar uri
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE|NICKNAME
     * @return String
     */
    fun toStringWithoutImage(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.Birthday}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}${IOHandler.tournamentDivider_properties}" +
                getStringFromValue(Identifier.Surname, this.surname) +
                getStringFromValue(Identifier.Patronymic, this.patronymic) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.Rating, this.rating) +
                getStringFromValue(Identifier.RankId, this.rankId) +
                getStringFromValue(Identifier.CountryId, this.countryId) +
                getStringFromValue(Identifier.CoachId, this.coachId)
    }

    companion object {
        const val Name: String = "EventUser"
    }
}