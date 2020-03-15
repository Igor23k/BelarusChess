package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.CountryPresenterCallBack

/**
 * Created by Igor on 12.04.2018.
 */

interface CountryPresenter : BasePresenter {
    fun getCountry(id: Int?)

    fun getCountries()

    fun attachView(countryPresenterCallBack: CountryPresenterCallBack)
}