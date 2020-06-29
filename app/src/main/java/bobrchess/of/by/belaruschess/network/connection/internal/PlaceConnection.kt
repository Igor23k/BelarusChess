package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackPlace
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Igor on 11.04.2018.
 */

class PlaceConnection {

    private var callBack: CallBackPlace? = null

    fun getPlace(id: Int?) {
        App.getPersonalServerApi().getPlace(id!!).enqueue(object : Callback<PlaceDTO> {
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
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getPlaces() {
        App.getPersonalServerApi().places.enqueue(object : Callback<List<PlaceDTO>> {
            override fun onResponse(call: Call<List<PlaceDTO>>, response: Response<List<PlaceDTO>>) {
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

            override fun onFailure(call: Call<List<PlaceDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }


    fun attachPresenter(callBack: CallBackPlace) {
        this.callBack = callBack
    }
}
