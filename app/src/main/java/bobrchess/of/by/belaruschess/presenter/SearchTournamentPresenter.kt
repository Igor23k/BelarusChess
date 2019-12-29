package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface SearchTournamentPresenter : BasePresenter {
    fun loadTournaments()
    fun loadTournaments(count: Int?)
    fun searchTournaments(test: String)
    fun attachView(activity: MainActivity)
}
