package bobrchess.of.by.belaruschess.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by Igor on 10.04.2018.
 */

class TournamentDTO : Serializable{

    var id: Long? = null

    var name: String? = null

    var shortDescription: String? = null

    var fullDescription: String? = null

    var startDate: String? = null

    var finishDate: String? = null

    var countPlayersInTeam: Int? = null

    var place: PlaceDTO? = null

    var referee: UserDTO? = null

    constructor(id: Long?, name: String, shortDescription: String, fullDescription: String, startDate: String, finishDate: String, countPlayersInTeam: Int?, place: PlaceDTO, referee: UserDTO) {
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

    constructor()
}
