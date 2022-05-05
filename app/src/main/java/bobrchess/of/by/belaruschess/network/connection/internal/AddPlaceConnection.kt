package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddPlace
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.buildOnFailureResponse
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddPlaceConnection {

    private var callBack: CallBackAddPlace? = null

    fun removePlace(id: Int?, authorizationHeader: String) {
        App.getPersonalServerApi().removePlace(authorizationHeader, id!!).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }


    fun addPlace(placeDTO: PlaceDTO, image: File?, authorizationHeader: String) {
        App.getPersonalServerApi().addPlace(authorizationHeader, placeDTO, Util.getMultipartImage(image)).enqueue(object : Callback<PlaceDTO> {
            override fun onResponse(call: Call<PlaceDTO>, response: Response<PlaceDTO>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<PlaceDTO>, t: Throwable) {
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }

    fun getCountries() {
        App.getPersonalServerApi().countries.enqueue(object : Callback<List<CountryDTO>> {
            override fun onResponse(call: Call<List<CountryDTO>>, response: Response<List<CountryDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onCountryResponse(response.body())
                    } else {
                        callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<List<CountryDTO>>, t: Throwable) {
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackAddPlace) {
        this.callBack = callBack
    }
}
