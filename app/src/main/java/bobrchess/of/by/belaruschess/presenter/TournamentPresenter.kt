package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.TournamentActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface TournamentPresenter : BasePresenter {
    fun getTournament(id: Int?)

    fun getTournaments()

    fun loadGames()

    fun attachView(activity: TournamentActivity)
}