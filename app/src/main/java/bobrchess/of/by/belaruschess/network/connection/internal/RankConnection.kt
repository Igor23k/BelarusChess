package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRank
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Igor on 11.04.2018.
 */

class RankConnection {

    private var callBack: CallBackRank? = null

    fun getRank(id: Int?) {
        App.getPersonalServerApi().getRank(id!!).enqueue(object : Callback<RankDTO> {
            override fun onResponse(call: Call<RankDTO>, response: Response<RankDTO>) {
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

            override fun onFailure(call: Call<RankDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getRanks() {
        App.getPersonalServerApi().ranks.enqueue(object : Callback<List<RankDTO>> {
            override fun onResponse(call: Call<List<RankDTO>>, response: Response<List<RankDTO>>) {
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

            override fun onFailure(call: Call<List<RankDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }


    fun attachPresenter(callBack: CallBackRank) {
        this.callBack = callBack
    }
}
