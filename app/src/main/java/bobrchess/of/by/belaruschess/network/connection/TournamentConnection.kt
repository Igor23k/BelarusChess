package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackTournament
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class TournamentConnection {

    private var callBack: CallBackTournament? = null

    fun getTournament(id: Int?) {
        App.getAPI().getTournament(id!!).enqueue(object : Callback<TournamentDTO> {
            override fun onResponse(call: Call<TournamentDTO>, response: Response<TournamentDTO>) {
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

            override fun onFailure(call: Call<TournamentDTO>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun getTournaments() {
        App.getAPI().tournaments.enqueue(object : Callback<List<TournamentDTO>> {
            override fun onResponse(call: Call<List<TournamentDTO>>, response: Response<List<TournamentDTO>>) {
                if (response.isSuccessful) {
                    callBack!!.onResponse(response.body())
                } else {
                    //тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<List<TournamentDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getGames() {
        App.getAPI().games.enqueue(object : Callback<List<GameDTO>> {
            override fun onResponse(call: Call<List<GameDTO>>, response: Response<List<GameDTO>>) {
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

            override fun onFailure(call: Call<List<GameDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }


    fun attachPresenter(callBack: CallBackTournament) {
        this.callBack = callBack
    }
}
