package bobrchess.of.by.belaruschess.network.connection.external

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayersDTO
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentsDataDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackFideApi
import bobrchess.of.by.belaruschess.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FideApiConnection {

    private var callBack: CallBackFideApi? = null

    fun searchTopPlayers(text: String) {
        App.getExternalFideServerApi().searchTopPlayers(text).enqueue(object : Callback<TopPlayersDTO> {
            override fun onResponse(call: Call<TopPlayersDTO>, response: Response<TopPlayersDTO>) {
                if (response.isSuccessful) {
                    callBack!!.onResponse(response.body())
                } else {
                    //todo тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<TopPlayersDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getTopPlayers() {
        App.getExternalFideServerApi().topPlayersRating.enqueue(object : Callback<TopPlayersDTO> {
            override fun onResponse(call: Call<TopPlayersDTO>, response: Response<TopPlayersDTO>) {
                if (response.isSuccessful) {
                    callBack!!.onResponse(response.body())
                } else {
                    //todo тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<TopPlayersDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getWorldTournaments(id: Int, worldChampion: Boolean, closestEvents: Boolean, category: String, dateStartMonth: Int) {
        App.getExternalFideServerApi().worldTournaments(id, worldChampion, closestEvents, category, dateStartMonth).enqueue(object : Callback<WorldTournamentsDataDTO> {
            override fun onResponse(call: Call<WorldTournamentsDataDTO>, response: Response<WorldTournamentsDataDTO>) {
                if (response.isSuccessful) {
                    callBack!!.onResponse(response.body())
                } else {
                    //todo тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<WorldTournamentsDataDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackFideApi) {
        this.callBack = callBack
    }
}
