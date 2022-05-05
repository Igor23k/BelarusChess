package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import java.text.DateFormat
import java.util.*

class EventTournament(
        var id: Int,
        var startDate: Date,
        var name: String) :
        EventDate(startDate, id) {

    /**
     * Identifier is an identifier for sorting
     */
    enum class Identifier : SortIdentifier {
        StartDate {
            override fun Identifier(): Int = 0
        },
        FinishDate {
            override fun Identifier(): Int = 1
        },
        Name {
            override fun Identifier(): Int = 2
        },
        ShortDescription {
            override fun Identifier(): Int = 3
        },
        FullDescription {
            override fun Identifier(): Int = 4
        },
        Id {
            override fun Identifier(): Int = 5
        },
        Image {
            override fun Identifier(): Int = 6
        },
        Referee {
            override fun Identifier(): Int = 7
        },
        CreatedBy {
            override fun Identifier(): Int = 8
        },
        Place {
            override fun Identifier(): Int = 9
        },
        ToursCount {
            override fun Identifier(): Int = 10
        }
    }

    var toursCount: Int? = null

    var shortDescription: String? = null

    var fullDescription: String? = null

    var finishDate: Date? = null

    var image: ByteArray? = null

    var refereeId: Long? = null

    var createdBy: Long? = null

    var placeId: Int? = null

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.StartDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.FinishDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}" +
                getStringFromValue(Identifier.ShortDescription, this.shortDescription) +
                getStringFromValue(Identifier.FullDescription, this.fullDescription) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.Image, this.image) +
                getStringFromValue(Identifier.Referee, this.refereeId) +
                getStringFromValue(Identifier.CreatedBy, this.createdBy) +
                getStringFromValue(Identifier.Place, this.placeId) +
                getStringFromValue(Identifier.ToursCount, this.toursCount)
    }

    /**
     * toString returns EventTournament as string representation but excludes a possible avatar uri
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE|NICKNAME
     * @return String
     */
    fun toStringWithoutImage(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.StartDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.FinishDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.finishDate!!,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}" +
                getStringFromValue(Identifier.ShortDescription, this.shortDescription) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.FullDescription, this.fullDescription) +
                getStringFromValue(Identifier.Referee, this.refereeId) +
                getStringFromValue(Identifier.CreatedBy, this.createdBy) +
                getStringFromValue(Identifier.Place, this.placeId) +
                getStringFromValue(Identifier.ToursCount, this.toursCount)
    }

    companion object {
        const val Name: String = "EventTournament"
    }
}