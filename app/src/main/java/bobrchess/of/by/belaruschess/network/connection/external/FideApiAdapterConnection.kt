package bobrchess.of.by.belaruschess.network.connection.external

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.externalFide.*
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAdapterFideApi
import bobrchess.of.by.belaruschess.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FideApiAdapterConnection {

    private var callBack: CallBackAdapterFideApi? = null

    fun getTopPlayer(id: Int) {
        App.getExternalFideServerApi().topPlayer(id).enqueue(object : Callback<List<TopPlayerWithImageDTO>> {
            override fun onResponse(call: Call<List<TopPlayerWithImageDTO>>, response: Response<List<TopPlayerWithImageDTO>>) {
                if (response.isSuccessful) {
                    callBack!!.onResponse(response.body())
                } else {
                    //todo тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<List<TopPlayerWithImageDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getWorldTournament(id: Int) {
        App.getExternalFideServerApi().worldTournament(id).enqueue(object : Callback<WorldTournamentDataDTO> {
            override fun onResponse(call: Call<WorldTournamentDataDTO>, response: Response<WorldTournamentDataDTO>) {
                if (response.isSuccessful) {
                     callBack!!.onResponse(response.body())
                } else {
                    //todo тут нужно блок экрана снимать и тд
                }
            }

            override fun onFailure(call: Call<WorldTournamentDataDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackAdapterFideApi) {
        this.callBack = callBack
    }
}
