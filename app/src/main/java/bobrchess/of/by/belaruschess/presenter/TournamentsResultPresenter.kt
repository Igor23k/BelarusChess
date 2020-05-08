package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.TournamentsResultContractView

/**
 * Created by Igor on 12.04.2018.
 */

interface TournamentsResultPresenter : BasePresenter {
    fun attachView(contractView: TournamentsResultContractView)

    fun loadUserTournamentsResults(id: Int)
}
