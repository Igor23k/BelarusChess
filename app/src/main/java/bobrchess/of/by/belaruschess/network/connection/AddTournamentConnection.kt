package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class AddTournamentConnection {

    private var callBack: CallBackAddTournament? = null

    fun addTournament(tournamentDTO: TournamentDTO) {
        App.getAPI().addTournament(tournamentDTO).enqueue(object : Callback<TournamentDTO> {
            override fun onResponse(call: Call<TournamentDTO>, response: Response<TournamentDTO>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<TournamentDTO>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getPlaces() {
        App.getAPI().places.enqueue(object : Callback<List<PlaceDTO>> {
            override fun onResponse(call: Call<List<PlaceDTO>>, response: Response<List<PlaceDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onPlaceResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<List<PlaceDTO>>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun getReferees() {
        App.getAPI().users.enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onRefereeResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun attachPresenter(callBack: CallBackAddTournament) {
        this.callBack = callBack
    }
}
