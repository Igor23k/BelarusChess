package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class SearchUserConnection {

    private var callBack: CallBackSearchUser? = null

    fun getUsers() {
        App.getAPI().users("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q").enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getUsers(count: Int?) {
        App.getAPI().getUsers(count!!).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun searchUsers(text: String) {
        App.getAPI().searchUsers(text).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun attachPresenter(callBack: CallBackSearchUser) {
        this.callBack = callBack
    }
}
