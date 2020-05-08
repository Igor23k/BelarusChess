package bobrchess.of.by.belaruschess.dto.extended

import bobrchess.of.by.belaruschess.dto.TournamentDTO
import java.io.Serializable

class ExtendedTournamentDTO : TournamentDTO(), Serializable {

    var selectedPlaceIndex: Int? = null
    var selectedRefereeIndex: Int? = null

}