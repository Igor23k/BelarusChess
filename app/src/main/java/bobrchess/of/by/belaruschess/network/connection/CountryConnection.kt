package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackCountry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class CountryConnection {

    private var callBack: CallBackCountry? = null

    fun getCountry(id: Int?) {
        App.getAPI().getCountry(id!!).enqueue(object : Callback<CountryDTO> {
            override fun onResponse(call: Call<CountryDTO>, response: Response<CountryDTO>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Throwable(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<CountryDTO>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun addCountry(countryDTO: CountryDTO) {
        App.getAPI().addCountry(countryDTO).enqueue(object : Callback<CountryDTO> {
            override fun onResponse(call: Call<CountryDTO>, response: Response<CountryDTO>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Throwable(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<CountryDTO>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }


    fun attachPresenter(callBack: CallBackCountry) {
        this.callBack = callBack
    }
}
