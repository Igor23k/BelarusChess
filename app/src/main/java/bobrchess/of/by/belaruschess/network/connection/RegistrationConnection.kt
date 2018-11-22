package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class RegistrationConnection {

    private var callBack: CallBackRegistration? = null

    fun registration(userDTO: UserDTO) {
        App.getAPI().registration(userDTO).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getCoaches() {
        App.getAPI().users.enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onCoachResponse(response.body().toMutableList())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getRanks() {
        App.getAPI().ranks.enqueue(object : Callback<List<RankDTO>> {
            override fun onResponse(call: Call<List<RankDTO>>, response: Response<List<RankDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onRankResponse(response.body().toMutableList())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<List<RankDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getCountries() {
        App.getAPI().countries.enqueue(object : Callback<List<CountryDTO>> {
            override fun onResponse(call: Call<List<CountryDTO>>, response: Response<List<CountryDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onCountryResponse(response.body().toMutableList())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<List<CountryDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun attachPresenter(callBack: CallBackRegistration) {
        this.callBack = callBack
    }
}
