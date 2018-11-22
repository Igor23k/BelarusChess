package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.GameActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface GamePresenter : BasePresenter {
    fun getGame(id: Int?)

    fun getGames()

    fun attachView(activity: GameActivity)
}