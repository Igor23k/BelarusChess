package bobrchess.of.by.belaruschess.dto

import java.io.Serializable
import java.util.Date

/**
 * Created by Igor on 10.04.2018.
 */

class TournamentDTO : Serializable{

    var id: Int? = null

    var name: String? = null

    var shortDescription: String? = null

    var fullDescription: String? = null

    var startDate: Date? = null

    var finishDate: Date? = null

    var countPlayersInTeam: Int? = null

    var place: PlaceDTO? = null

    var referee: UserDTO? = null

    constructor(id: Int?, name: String, shortDescription: String, fullDescription: String, startDate: Date, finishDate: Date, countPlayersInTeam: Int?, place: PlaceDTO, referee: UserDTO) {
        this.id = id
        this.name = name
        this.shortDescription = shortDescription
        this.fullDescription = fullDescription
        this.startDate = startDate
        this.finishDate = finishDate
        this.countPlayersInTeam = countPlayersInTeam
        this.place = place
        this.referee = referee
    }

    constructor() {}
}
