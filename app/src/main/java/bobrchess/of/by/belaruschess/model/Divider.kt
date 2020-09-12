package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.util.Constants
import java.text.DateFormat
import java.util.*

class Divider(date: Date, val text: String) : EventDate(date) {

    /**
     * Identifier is an identifier for sorting
     */
    enum class Identifier : SortIdentifier {
        Date {
            override fun Identifier(): Int = 0
        },
        Text {
            override fun Identifier(): Int = 1
        }
    }

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Date}${IOHandler.tournamentDivider_values}${parseDateToString(
                this.eventDate,
                DateFormat.DEFAULT,
                Locale.GERMAN
        )}${IOHandler.tournamentDivider_properties}${Identifier.Text}${IOHandler.tournamentDivider_values}$text"
    }

    companion object {
        const val Name: String = "Divider"
    }
}