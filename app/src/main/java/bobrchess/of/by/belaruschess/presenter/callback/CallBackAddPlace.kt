package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackAddPlace : CallBack {
    fun onResponse(placeDTO: PlaceDTO)
    fun onCountryResponse(countries: List<CountryDTO>)
    fun onResponse(removedPlaceId:Int)
}
