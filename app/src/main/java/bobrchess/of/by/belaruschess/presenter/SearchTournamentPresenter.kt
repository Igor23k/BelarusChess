package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView

interface SearchTournamentPresenter : BasePresenter {
    fun loadTournaments()
    fun loadTournaments(count: Int?)
    fun searchTournaments(text: String)
    fun attachView(searchTournamentContractView: SearchTournamentContractView)
}
