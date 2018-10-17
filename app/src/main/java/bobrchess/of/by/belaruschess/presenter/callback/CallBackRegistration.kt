package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackRegistration : CallBack {
    fun onResponse(userDTO: UserDTO)
    fun onCoachResponse(coaches: List<UserDTO>)
    fun onRankResponse(ranks: List<RankDTO>)
    fun onCountryResponse(countries: List<CountryDTO>)
}
