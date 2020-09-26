package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser
import bobrchess.of.by.belaruschess.util.Util
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Igor on 11.04.2018.
 */

class SearchUserConnection {

    private var callBack: CallBackSearchUser? = null
    fun getCoaches(authorizationHeader: String) {
        App.getPersonalServerApi().users(authorizationHeader).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getUsers(count: Int?) {
        App.getPersonalServerApi().getUsers(count!!).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getReferees(authorizationHeader: String) {
        App.getPersonalServerApi().users(authorizationHeader).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun getUserById(authorizationHeader: String, id: Int) {
        App.getPersonalServerApi().userById(authorizationHeader, id).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
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

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun searchUsers(text: String) {
        App.getPersonalServerApi().searchUsers(text).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackSearchUser) {
        this.callBack = callBack
    }
}
