package bobrchess.of.by.belaruschess.model

import android.util.Log
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.util.Constants
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OneTimeEvent(_eventdate: Date?, var name: String) : EventDate(_eventdate) {

    /**
     * Identifier is an identifier for sorting
     * also used for map-like parsing for reading/writing
     */
    enum class Identifier : SortIdentifier {
        Date {
            override fun Identifier(): Int = 0
        },
        Name {
            override fun Identifier(): Int = 1
        },
        Note {
            override fun Identifier(): Int = 2
        }
    }

    var note: String? = null
        get() {
            return if (field == null) {
                null
            } else if (field!!.isEmpty() || field!!.isBlank()) {
                Log.d("OneTimeEvent", "member var NOTE is blank/empty when trying to access it")
                null
            } else {
                field!!.trim()
            }
        }
        set(value) {
            if (value == null) {
                Log.d("OneTimeEvent", "member variable NOTE was set to a null value!")
                field = null
            } else field = if (value.isBlank() || value.isEmpty()) {
                Log.d("OneTimeEvent", "member variable NOTE was set to an empty/blank value!")
                null
            } else {
                value
            }
        }

    fun getYearsUntil(): Int {
        val futureDateCal = Calendar.getInstance()
        futureDateCal.time = this.eventDate

        return (futureDateCal.get(Calendar.YEAR) - Calendar.getInstance().get(Calendar.YEAR))
    }

    /**
     * dateIsExpired returns false if the startDate is in the future
     * If this returns true, the event expired
     * @return Boolean
     */
    fun dateIsExpired(): Boolean {
        if (this.getYear() == Calendar.getInstance().get(Calendar.YEAR)) {
            return (this.getDayOfYear() < Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
        } else {
            return false
        }
    }

    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}" +
                "${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}" +
                "${IOHandler.tournamentDivider_properties}${Identifier.Date}${IOHandler.tournamentDivider_values}${parseDateToString(
                    this.eventDate,
                    DateFormat.DEFAULT,
                    Constants.BELARUS_LOCALE
                )}" +
                getStringFromValue(
                        Identifier.Note,
                    this.note
                )
    }

    override fun getDaysUntil(): Int {
        val currentDayCal = Calendar.getInstance()
        currentDayCal.set(Calendar.HOUR_OF_DAY, 0)
        currentDayCal.set(Calendar.MINUTE, 0)
        currentDayCal.set(Calendar.SECOND, 0)
        currentDayCal.set(Calendar.MILLISECOND, 0)

        val nextYear = Calendar.getInstance()
        nextYear.time = eventDate

        if (nextYear.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
            nextYear.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
        ) {
            return 0
        }

        return TimeUnit.MILLISECONDS.toDays(nextYear.timeInMillis - currentDayCal.timeInMillis)
            .toInt()
    }

    companion object {
        const val Name: String = "OneTimeEvent"
    }
}