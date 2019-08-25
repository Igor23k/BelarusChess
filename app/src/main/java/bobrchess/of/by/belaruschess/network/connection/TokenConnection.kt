package bobrchess.of.by.belaruschess.network.connection

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.TokenDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackToken
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenConnection {

    private var callBack: CallBackToken? = null

    fun refreshToken(authorization: String) {
        App.getAPI().refreshToken(authorization).enqueue(object : Callback<TokenDTO> {
            override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
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

            override fun onFailure(call: Call<TokenDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackToken) {
        this.callBack = callBack
    }
}
