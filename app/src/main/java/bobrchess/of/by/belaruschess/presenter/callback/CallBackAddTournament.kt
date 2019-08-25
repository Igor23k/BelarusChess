package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackAddTournament : CallBack {
    fun onResponse(tournamentDTO: TournamentDTO)
    fun onRefereeResponse(referees: List<UserDTO>)
    fun onPlaceResponse(places: List<PlaceDTO>)
}
