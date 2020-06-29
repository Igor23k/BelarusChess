package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.FideApiContractView


interface FideApiPresenter : BasePresenter {
    fun loadTopPlayersRating()
    fun loadTournaments(id: Int, worldChampion: Boolean, closestEvents: Boolean, category: String, dateStartMonth: Int)
    fun attachView(fideApiContractView: FideApiContractView)
}
