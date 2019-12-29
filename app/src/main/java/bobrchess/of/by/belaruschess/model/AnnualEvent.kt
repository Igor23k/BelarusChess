package com.procrastimax.birthdaybuddy.models

import android.util.Log
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.EventDate
import java.text.DateFormat
import java.util.*

class AnnualEvent(_eventDate: Date, var name: String, var hasStartYear: Boolean) :
    EventDate(_eventDate, TournamentDTO()) {

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
        HasStartYear {
            override fun Identifier(): Int = 2
        },
        Note {
            override fun Identifier(): Int = 3
        },
    }

    var note: String? = null
        get() {
            return if (field == null) {
                null
            } else if (field!!.isEmpty() || field!!.isBlank()) {
                Log.d("AnnualEvent", "member var NOTE is blank/empty when trying to access it")
                null
            } else {
                field!!.trim()
            }
        }
        set(value) {
            if (value == null) {
                Log.d("AnnualEvent", "member variable NOTE was set to a null value!")
                field = null
            } else field = if (value.isBlank() || value.isEmpty()) {
                Log.d("AnnualEvent", "member variable NOTE was set to an empty/blank value!")
                null
            } else {
                value
            }
        }

    /**
     * getXTimesSinceStarting returns a int value representing how many times this annual event took place in the past
     * This respects the current day of the happening. So when an annual event takes place on a day, it takes place for the x.th time and not the x+1.th time
     */
    fun getXTimesSinceStarting(): Int {
        val currCal = Calendar.getInstance()

        if (currCal.get(Calendar.DAY_OF_MONTH) == this.getDayOfMonth() && currCal.get(Calendar.MONTH) == this.getMonth() && currCal.get(
                Calendar.YEAR
            ) == this.getYear()
        ) {
            return 0
        }

        return getYearsSince() + 1
    }

    /**
     * toString returns EventTournament as string representation
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE
     * @return String
     */
    override fun toString(): String {
        return "$Name${IOHandler.characterDivider_properties}" +
                "${Identifier.Name}${IOHandler.characterDivider_values}${this.name}" +
                "${IOHandler.characterDivider_properties}${Identifier.Date}${IOHandler.characterDivider_values}${parseDateToString(
                    this.eventDate,
                    DateFormat.DEFAULT,
                    Locale.GERMAN
                )}${IOHandler.characterDivider_properties}" +
                "${Identifier.HasStartYear}${IOHandler.characterDivider_values}${this.hasStartYear}" +
                getStringFromValue(
                    Identifier.Note,
                    this.note
                )
    }

    companion object {
        const val Name: String = "AnnualEvent"
    }
}