package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.SearchPlaceContractView
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView

/**
 * Created by Igor on 12.04.2018.
 */

interface SearchPlacePresenter : BasePresenter {
    fun loadPlaces()
    fun searchPlaces(text: String)
    fun attachView(searchPlaceContractView: SearchPlaceContractView)
}
