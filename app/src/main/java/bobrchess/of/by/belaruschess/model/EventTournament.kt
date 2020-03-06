package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import java.text.DateFormat
import java.util.*

/**
 * EventTournament is model class to store basic data about a persons birthday
 *
 * It inherits from EventDay, so it uses a StartDate, and Strings for the name of the described person
 * ShortDescription cant be null, if it shouldn't be set, use "0" to mark the shortDescription as unwanted property when f.e. don't show it in UI
 * isYearGiven is flag to indicate whether the birthday-year is known/given
 *
 * TODO:
 *  - add possibility for favorites
 *
 *  @param startDate : StartDate
 *  @param name : String
 *  @param isYearGiven : Boolean
 * @author Procrastimax
 */
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
        AvatarUri {
            override fun Identifier(): Int = 6
        },
        Referee {
            override fun Identifier(): Int = 7
        },
        Place {
            override fun Identifier(): Int = 8
        }
    }

    var shortDescription: String? = null

    var fullDescription: String? = null

    var finishDate: Date? = null

    var imageUri: String? = null

    var refereeId: Long? = null

    var placeId: Int? = null

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
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.StartDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN
                )}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.FinishDate}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Locale.GERMAN//todo why??? выше то же
                )}" +
                getStringFromValue(Identifier.ShortDescription, this.shortDescription) +
                getStringFromValue(Identifier.FullDescription, this.fullDescription) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.AvatarUri, this.imageUri) +
                getStringFromValue(Identifier.Referee, this.refereeId) +
                getStringFromValue(Identifier.Place, this.placeId)

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
                        Locale.GERMAN//todo why??? выше то же
                )}" +
                getStringFromValue(Identifier.ShortDescription, this.shortDescription) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.FullDescription, this.fullDescription) +
                getStringFromValue(Identifier.Referee, this.refereeId) +
                getStringFromValue(Identifier.Place, this.placeId)
    }

    companion object {
        const val Name: String = "EventTournament"
    }
}