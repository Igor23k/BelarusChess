package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class UserInfoConnection {

    private var callBack: CallBackUserInfo? = null

    fun getTournaments() {
        App.getAPI().tournaments.enqueue(object : Callback<List<TournamentDTO>> {
            override fun onResponse(call: Call<List<TournamentDTO>>, response: Response<List<TournamentDTO>>) {
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

            override fun onFailure(call: Call<List<TournamentDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun attachPresenter(callBack: CallBackUserInfo) {
        this.callBack = callBack
    }

    internal fun processResponse() {

    }
}
