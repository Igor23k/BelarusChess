package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayersDTO
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentsDataDTO


interface CallBackFideApi : CallBack {
    fun onResponse(tournamentsData: WorldTournamentsDataDTO)
    fun onResponse(topPlayersDTO: TopPlayersDTO)
}
