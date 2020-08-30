package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.util.Constants
import java.text.DateFormat
import java.util.*

class EventTournamentResult(
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
        Name {
            override fun Identifier(): Int = 1
        },
        Position {
            override fun Identifier(): Int = 2
        },
        Points {
            override fun Identifier(): Int = 3
        },
        Image {
            override fun Identifier(): Int = 4
        },
        Id {
            override fun Identifier(): Int = 5
        }
    }

    var position: Int? = null
    var points: Double? = null
    var imageUri: String? = null

    /**
     * toString returns EventTournament as string representation
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE|AVATARURI|NICKNAME
     * @return String
     */
    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.StartDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Constants.BELARUS_LOCALE
                )}${IOHandler.tournamentDivider_properties}" +
                getStringFromValue(Identifier.Name, this.name) +
                getStringFromValue(Identifier.Position, this.position) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.Points, this.points) +
                getStringFromValue(Identifier.Image, this.imageUri)
    }

    companion object {
        const val Name: String = "EventTournamentResult"
    }
}