package com.procrastimax.birthdaybuddy.models

import android.util.Log
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.EventDate
import java.text.DateFormat
import java.util.*

/**
 * EventTournament is model class to store basic data about a persons birthday
 *
 * It inherits from EventDay, so it uses a Date, and Strings for the name of the described person
 * ShortDescription cant be null, if it shouldn't be set, use "0" to mark the shortDescription as unwanted property when f.e. don't show it in UI
 * isYearGiven is flag to indicate whether the birthday-year is known/given
 *
 * TODO:
 *  - add possibility for favorites
 *
 *  @param date : Date
 *  @param name : String
 *  @param isYearGiven : Boolean
 * @author Procrastimax
 */
class EventTournament(
        public var date: Date,
        public var name: String,
        public var tournamentDTO: TournamentDTO,
        var isYearGiven: Boolean = true
) :
        EventDate(date, tournamentDTO) {

    /**
     * Identifier is an identifier for sorting
     */
    enum class Identifier : SortIdentifier {
        Date {
            override fun Identifier(): Int = 0
        },
        Name {
            override fun Identifier(): Int = 1
        },
        ShortDescription {
            override fun Identifier(): Int = 2
        },
        FullDescription {
            override fun Identifier(): Int = 3
        }/*,
        Note {
            override fun Identifier(): Int = 4
        }*/,
        AvatarUri {
            override fun Identifier(): Int = 5
        },
        /* Nickname {
             override fun Identifier(): Int = 6
         }*/
    }

    var shortDescription: String? = null

/*
    var note: String? = null
*/

    var fullDescription: String? = null

    var imageUri: String? = null

    fun getNicknameOrForename(): String {
        return if (this.fullDescription != null) {
            this.fullDescription!!
        } else {
            name
        }
    }

    /**
     * getTurningAgeValue returns a value which represents the value of a person turning a specific age
     * This respects that on a birthday-day a person is still turning the age beforehand and not the age+1
     */
    /* fun getTurningAgeValue(): Int {
         val currCal = Calendar.getInstance()

         if (currCal.get(Calendar.DAY_OF_MONTH) == this.getDayOfMonth() && currCal.get(Calendar.MONTH) == this.getMonth() && currCal.get(
                 Calendar.YEAR
             ) == this.getYear()
         ) {
             return 0
         }

         return getYearsSince() + 1
     }*/

    /**
     * toString returns EventTournament as string representation
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE|AVATARURI|NICKNAME
     * @return String
     */
    override fun toString(): String {
        return "$Name${IOHandler.characterDivider_properties}${Identifier.Name}${IOHandler.characterDivider_values}${this.name}${IOHandler.characterDivider_properties}" +
                "${Identifier.Date}${IOHandler.characterDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}" +
                "${IOHandler.characterDivider_properties}${Identifier.FullDescription}${IOHandler.characterDivider_values}${this.isYearGiven}" +
                getStringFromValue(Identifier.ShortDescription, this.shortDescription) +
                //  getStringFromValue(Identifier.Note, this.note) +
                getStringFromValue(Identifier.AvatarUri, this.imageUri) //+
        //  getStringFromValue(Identifier.Nickname, this.fullDescription)
    }

    /**
     * toString returns EventTournament as string representation but excludes a possible avatar uri
     * This is "optimized" for Serialization, so THE FIRST WORD HAS TO BE THE TYPIFICATION f.e. "Birthday"
     * returned string follows the pattern TYPE|FORENAME|SURNAME|EVENTDATE|ISYEARGIVEN|NOTE|NICKNAME
     * @return String
     */
    fun toStringWithoutImage(): String {
        return "$Name${IOHandler.characterDivider_properties}${Identifier.Name}${IOHandler.characterDivider_values}${this.name}${IOHandler.characterDivider_properties}" +
                "${Identifier.Date}${IOHandler.characterDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}" +
                "${IOHandler.characterDivider_properties}${Identifier.FullDescription}${IOHandler.characterDivider_values}${this.isYearGiven}" +
                getStringFromValue(Identifier.ShortDescription, this.shortDescription)// +
        //    getStringFromValue(Identifier.Note, this.note) +
        //   getStringFromValue(Identifier.Nickname, this.fullDescription)
    }

    companion object {
        const val Name: String = "EventTournament"
    }
}