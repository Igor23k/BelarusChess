package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

open class TournamentDTO : Serializable {

    var id: Long = 0
    var toursCount: Int = 0
    var name: String? = null
    var fullDescription: String? = null
    var startDate: String? = null
    var finishDate: String? = null
    var place: PlaceDTO? = null
    var referee: UserDTO? = null
    var createdBy: UserDTO? = null
    var image: ByteArray? = null

    constructor(tournamentDTO: TournamentDTO) {
        this.id = tournamentDTO.id
        this.toursCount = tournamentDTO.toursCount
        this.name = tournamentDTO.name
        this.fullDescription = tournamentDTO.fullDescription
        this.startDate = tournamentDTO.startDate
        this.finishDate = tournamentDTO.finishDate
        this.place = tournamentDTO.place
        this.referee = tournamentDTO.referee
        this.createdBy = tournamentDTO.createdBy
        this.image = tournamentDTO.image
    }

    constructor(id: Long, toursCount: Int, name: String, fullDescription: String, startDate: String,
                finishDate: String, place: PlaceDTO, referee: UserDTO, createdBy: UserDTO, imageUri: ByteArray) {
        this.id = id
        this.toursCount = toursCount
        this.name = name
        this.fullDescription = fullDescription
        this.startDate = startDate
        this.finishDate = finishDate
        this.place = place
        this.referee = referee
        this.createdBy = createdBy
        this.image = imageUri
    }

    constructor()
}
