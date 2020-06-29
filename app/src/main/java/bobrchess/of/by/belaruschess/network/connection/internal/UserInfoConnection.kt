package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.TournamentResultDTO
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUserInfo
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoConnection {

    private var callBack: CallBackUserInfo? = null

    fun getTournamentsResults(id: Int) {
        App.getPersonalServerApi().getTournamentsResultByUser(id).enqueue(object : Callback<List<TournamentResultDTO>> {
            override fun onResponse(call: Call<List<TournamentResultDTO>>, response: Response<List<TournamentResultDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        var a = response.body()
                        var b = null;
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<List<TournamentResultDTO>>, t: Throwable) {//todo тут если ошибка то ничего не происходит + нужно сделать что даже если турнирыне прилетели то страницу пользователя все равно показать
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackUserInfo) {
        this.callBack = callBack
    }
}
