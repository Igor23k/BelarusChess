package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

open class TournamentDTO : Serializable {

    var id: Long = 0
    var toursCount: Int = 0
    var name: String? = null
    var shortDescription: String? = null
    var fullDescription: String? = null
    var startDate: String? = null
    var finishDate: String? = null
    var countPlayersInTeam: Int? = null
    var place: PlaceDTO? = null
    var referee: UserDTO? = null
    var image: String? = null

    constructor(tournamentDTO: TournamentDTO) {
        this.id = tournamentDTO.id
        this.toursCount = tournamentDTO.toursCount
        this.name = tournamentDTO.name
        this.shortDescription = tournamentDTO.shortDescription
        this.fullDescription = tournamentDTO.fullDescription
        this.startDate = tournamentDTO.startDate
        this.finishDate = tournamentDTO.finishDate
        this.countPlayersInTeam = tournamentDTO.countPlayersInTeam
        this.place = tournamentDTO.place
        this.referee = tournamentDTO.referee
        this.image = tournamentDTO.image
    }

    constructor(id: Long, toursCount: Int, name: String, shortDescription: String, fullDescription: String, startDate: String,
                finishDate: String, countPlayersInTeam: Int?, place: PlaceDTO, referee: UserDTO, image: String) {
        this.id = id
        this.toursCount = toursCount
        this.name = name
        this.shortDescription = shortDescription
        this.fullDescription = fullDescription
        this.startDate = startDate
        this.finishDate = finishDate
        this.countPlayersInTeam = countPlayersInTeam
        this.place = place
        this.referee = referee
        this.image = image
    }

    constructor()
}
