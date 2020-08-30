package bobrchess.of.by.belaruschess.adapter

import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerWithImageDTO
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentDTO
import bobrchess.of.by.belaruschess.presenter.BasePresenter


interface FideApiAdapterContract : BasePresenter {
    fun showWorldTournament(worldTournamentDTO: WorldTournamentDTO?)
    fun showTopPlayer(topPlayerDTO: TopPlayerWithImageDTO?)
}
