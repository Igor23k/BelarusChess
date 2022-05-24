package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.buildOnFailureResponse
import bobrchess.of.by.belaruschess.util.Util.Companion.getMultipartImage
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddTournamentConnection {

    private var callBack: CallBackAddTournament? = null

    fun removeTournament(id: Long?) {
        App.getPersonalServerApi().removeTournament(id!!).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
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

            override fun onFailure(call: Call<Long>, t: Throwable) {
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }

    fun addTournament(tournamentDTO: TournamentDTO, image: File?, authorizationHeader: String, isImageUpdated: Boolean) {
        App.getPersonalServerApi().addTournament(authorizationHeader, tournamentDTO, getMultipartImage(image), isImageUpdated).enqueue(object : Callback<TournamentDTO> {
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
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }

    fun getPlaces() {
        App.getPersonalServerApi().places.enqueue(object : Callback<List<PlaceDTO>> {
            override fun onResponse(call: Call<List<PlaceDTO>>, response: Response<List<PlaceDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onPlaceResponse(response.body())
                    } else {
                        callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<List<PlaceDTO>>, t: Throwable) {
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }

    fun getReferees(authorizationHeader: String) {
        App.getPersonalServerApi().referees(authorizationHeader).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onRefereeResponse(response.body())
                    } else {
                        callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(Util.buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackAddTournament) {
        this.callBack = callBack
    }
}
