package bobrchess.of.by.belaruschess.network.connection

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGame
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Igor on 11.04.2018.
 */

class GameConnection {

    private var callBack: CallBackGame? = null

    fun getGame(id: Int?) {
        App.getAPI().getGame(id!!).enqueue(object : Callback<GameDTO> {
            override fun onResponse(call: Call<GameDTO>, response: Response<GameDTO>) {
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

            override fun onFailure(call: Call<GameDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
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


    fun attachPresenter(callBack: CallBackGame) {
        this.callBack = callBack
    }
}
