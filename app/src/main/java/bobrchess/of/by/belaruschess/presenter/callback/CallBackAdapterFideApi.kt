package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerWithImageDTO
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentDataDTO


interface CallBackAdapterFideApi : CallBack {
    fun onResponse(tournamentDTO: WorldTournamentDataDTO)
    fun onResponse(topPlayerDTO: List<TopPlayerWithImageDTO>)
}
