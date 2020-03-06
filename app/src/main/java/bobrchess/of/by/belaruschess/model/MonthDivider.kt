package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import java.text.DateFormat
import java.util.*

class MonthDivider(date: Date, val month_name: String) : EventDate(date) {

    /**
     * Identifier is an identifier for sorting
     */
    enum class Identifier : SortIdentifier {
        Date {
            override fun Identifier(): Int = 0
        },
        MonthName {
            override fun Identifier(): Int = 1
        }
    }

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Date}${IOHandler.tournamentDivider_values}${parseDateToString(
                this.eventDate,
                DateFormat.DEFAULT,
                Locale.GERMAN
        )}${IOHandler.tournamentDivider_properties}${Identifier.MonthName}${IOHandler.tournamentDivider_values}$month_name"
    }

    companion object {
        const val Name: String = "MonthDivider"
    }
}