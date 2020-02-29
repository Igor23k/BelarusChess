package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.RankPresenterCallBack

/**
 * Created by Igor on 12.04.2018.
 */

interface RankPresenter : BasePresenter {
    fun getRank(id: Int?)

    fun getRanks()

    fun attachView(rankPresenterCallBack: RankPresenterCallBack)
}