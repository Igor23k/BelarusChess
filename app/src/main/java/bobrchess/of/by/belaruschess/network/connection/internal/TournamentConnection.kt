package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackTournament
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Igor on 11.04.2018.
 */

class TournamentConnection {

    private var callBack: CallBackTournament? = null

    fun getTournament(id: Int?) {
        App.getPersonalServerApi().getTournament(id!!).enqueue(object : Callback<TournamentDTO> {
            override fun onResponse(call: Call<TournamentDTO>, response: Response<TournamentDTO>) {
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

            override fun onFailure(call: Call<TournamentDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }


    fun getTournaments() {
        App.getPersonalServerApi().tournaments.enqueue(object : Callback<List<TournamentDTO>> {
            override fun onResponse(call: Call<List<TournamentDTO>>, response: Response<List<TournamentDTO>>) {
                if (response.isSuccessful) {
                    callBack!!.onResponse(response.body())
                } else {
                    //todo тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<List<TournamentDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getGames() {
        App.getPersonalServerApi().games.enqueue(object : Callback<List<GameDTO>> {
            override fun onResponse(call: Call<List<GameDTO>>, response: Response<List<GameDTO>>) {
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

            override fun onFailure(call: Call<List<GameDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }


    fun attachPresenter(callBack: CallBackTournament) {
        this.callBack = callBack
    }
}
