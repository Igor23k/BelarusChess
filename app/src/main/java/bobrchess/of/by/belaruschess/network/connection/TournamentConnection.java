package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackTournament;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE;
import static bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST;

/**
 * Created by Igor on 11.04.2018.
 */

public class TournamentConnection {

    private CallBackTournament callBack;

    public void getTournament(Integer id) {
        App.getAPI().getTournament(id).enqueue(new Callback<TournamentDTO>() {
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
                callBack.onFailure(t);
            }
        });
    }

    public void getTournaments() {
        App.getAPI().getTournaments().enqueue(new Callback<List<TournamentDTO>>() {
            @Override
            public void onResponse(Call<List<TournamentDTO>> call, Response<List<TournamentDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                } else {
                    //тут нужно блок экрана снимать и тд
                }
            }

            @Override
            public void onFailure(Call<List<TournamentDTO>> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void getGames() {
        App.getAPI().getGames().enqueue(new Callback<List<GameDTO>>() {
            @Override
            public void onResponse(Call<List<GameDTO>> call, Response<List<GameDTO>> response) {
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
            public void onFailure(Call<List<GameDTO>> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }


    public void attachPresenter(CallBackTournament callBack) {
        this.callBack = callBack;
    }
}
