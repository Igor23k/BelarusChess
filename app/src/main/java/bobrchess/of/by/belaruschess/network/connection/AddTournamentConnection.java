package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE;
import static bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST;

/**
 * Created by Igor on 11.04.2018.
 */

public class AddTournamentConnection {

    private CallBackAddTournament callBack;

    public void addTournament(TournamentDTO tournamentDTO) {
        App.getAPI().addTournament(tournamentDTO).enqueue(new Callback<TournamentDTO>() {
            @Override
            public void onResponse(Call<TournamentDTO> call, Response<TournamentDTO> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack.onResponse(response.body());
                    } else {
                        callBack.onFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<TournamentDTO> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void getPlaces() {
        App.getAPI().getPlaces().enqueue(new Callback<List<PlaceDTO>>() {
            @Override
            public void onResponse(Call<List<PlaceDTO>> call, Response<List<PlaceDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack.onPlaceResponse(response.body());
                    } else {
                        callBack.onFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<List<PlaceDTO>> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void getReferees() {
        App.getAPI().getUsers().enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack.onRefereeResponse(response.body());
                    } else {
                        callBack.onFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void attachPresenter(CallBackAddTournament callBack) {
        this.callBack = callBack;
    }
}
