package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.GamesListActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface GamesListPresenter : BasePresenter {
    fun loadGames()

    fun loadGames(count: Int?)

    fun searchGames()

    fun attachView(activity: GamesListActivity)
}
