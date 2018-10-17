package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.CountryDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackCountry : CallBack {
    fun onResponse(countryDTO: CountryDTO)

    fun onResponse(countryDTO: List<CountryDTO>)
}
