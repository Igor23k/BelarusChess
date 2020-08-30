package bobrchess.of.by.belaruschess.model

import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.util.Constants
import java.text.DateFormat
import java.util.*

class EventTopPlayer(
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
        Id {
            override fun Identifier(): Int = 4
        },
        ImageUri {
            override fun Identifier(): Int = 5
        },
        Position {
            override fun Identifier(): Int = 6
        },
        Rating {
            override fun Identifier(): Int = 7
        },
        BlitzRating {
            override fun Identifier(): Int = 8
        },
        RapidRating {
            override fun Identifier(): Int = 9
        },
        TopType {
            override fun Identifier(): Int = 10
        },
        Sex {
            override fun Identifier(): Int = 11
        },
        Country {
            override fun Identifier(): Int = 12
        }
    }

    var image: String? = null
    var position: Int? = null
    var rating: Int? = null
    var blitzRating: Int? = null
    var rapidRating: Int? = null
    var topType: String? = null
    var sex: String? = null
    var country: String? = null


    override fun toString(): String {
        return "$Name${IOHandler.tournamentDivider_properties}${Identifier.Name}${IOHandler.tournamentDivider_values}${this.name}${IOHandler.tournamentDivider_properties}" +
                "${Identifier.Birthday}${IOHandler.tournamentDivider_values}${parseDateToString(
                        this.eventDate,
                        DateFormat.DEFAULT,
                        Constants.BELARUS_LOCALE
                )}${IOHandler.tournamentDivider_properties}" +
                getStringFromValue(Identifier.Surname, this.surname) +
                getStringFromValue(Identifier.Id, this.id) +
                getStringFromValue(Identifier.ImageUri, this.image) +
                getStringFromValue(Identifier.Position, this.position) +
                getStringFromValue(Identifier.Rating, this.rating) +
                getStringFromValue(Identifier.BlitzRating, this.blitzRating) +
                getStringFromValue(Identifier.RapidRating, this.rapidRating) +
                getStringFromValue(Identifier.TopType, this.topType) +
                getStringFromValue(Identifier.Sex, this.sex) +
                getStringFromValue(Identifier.Country, this.country)
    }

    companion object {
        const val Name: String = "EventTopPlayer"
    }
}