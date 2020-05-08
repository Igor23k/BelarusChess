package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.PlacePresenterCallBack

/**
 * Created by Igor on 12.04.2018.
 */

interface PlacePresenter : BasePresenter {
    fun getPlace(id: Int?)
    fun getPlaces()
    fun attachView(placePresenterCallBack: PlacePresenterCallBack)
}