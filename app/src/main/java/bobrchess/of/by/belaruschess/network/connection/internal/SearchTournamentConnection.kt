package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchTournament
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTournamentConnection {

    private var callBack: CallBackSearchTournament? = null

    fun getTournaments(upcomingOnly: Boolean) {
        App.getPersonalServerApi().tournaments(upcomingOnly).enqueue(object : Callback<List<TournamentDTO>> {
            override fun onResponse(call: Call<List<TournamentDTO>>, response: Response<List<TournamentDTO>>) {
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

            override fun onFailure(call: Call<List<TournamentDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getTournaments(count: Int, upcomingOnly: Boolean) {
        App.getPersonalServerApi().tournaments(count, upcomingOnly).enqueue(object : Callback<List<TournamentDTO>> {
            override fun onResponse(call: Call<List<TournamentDTO>>, response: Response<List<TournamentDTO>>) {
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

            override fun onFailure(call: Call<List<TournamentDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun searchTournaments(text: String) {
        App.getPersonalServerApi().searchTournaments(text).enqueue(object : Callback<List<TournamentDTO>> {
            override fun onResponse(call: Call<List<TournamentDTO>>, response: Response<List<TournamentDTO>>) {
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

            override fun onFailure(call: Call<List<TournamentDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackSearchTournament) {
        this.callBack = callBack
    }
}
