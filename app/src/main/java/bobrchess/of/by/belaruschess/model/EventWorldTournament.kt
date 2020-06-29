package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import java.text.DateFormat
import java.util.*

class EventWorldTournament(
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
        Country {
            override fun Identifier(): Int = 3
        },
        City {
            override fun Identifier(): Int = 4
        },
        EventType {
            override fun Identifier(): Int = 5
        },
        ToursCount {
            override fun Identifier(): Int = 5
        }
    }

    var toursCount: Int? = null
    var finishDate: Date? = null
    var country: String? = null
    var city: String? = null
    var eventType: String? = null

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.StartDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.FinishDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.finishDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN//todo why??? выше то же
                )}" +
                getStringFromValue(Identifier.ToursCount, this.toursCount) +
                getStringFromValue(Identifier.Country, this.country) +
                getStringFromValue(Identifier.City, this.city) +
                getStringFromValue(Identifier.EventType, this.eventType)
    }

    companion object {
        const val Name: String = "EventWorldTournament"
    }
}