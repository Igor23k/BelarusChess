package bobrchess.of.by.belaruschess.network.connection

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.TournamentResultDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUserInfo
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Igor on 11.04.2018.
 */

class UserInfoConnection {

    private var callBack: CallBackUserInfo? = null

    fun getTournamentsResults() {
        App.getAPI().getTournamentsResultByUser(4).enqueue(object : Callback<List<TournamentResultDTO>> {
            override fun onResponse(call: Call<List<TournamentResultDTO>>, response: Response<List<TournamentResultDTO>>) {
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

            override fun onFailure(call: Call<List<TournamentResultDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackUserInfo) {
        this.callBack = callBack
    }
}
