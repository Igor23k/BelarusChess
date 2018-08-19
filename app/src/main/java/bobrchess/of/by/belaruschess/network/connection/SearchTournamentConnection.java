package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchTournament;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE;
import static bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST;

/**
 * Created by Igor on 11.04.2018.
 */

public class SearchTournamentConnection {

    private CallBackSearchTournament callBack;

    public void getTournaments() {
        App.getAPI().getTournaments().enqueue(new Callback<List<TournamentDTO>>() {
            @Override
            public void onResponse(Call<List<TournamentDTO>> call, Response<List<TournamentDTO>> response) {
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
            public void onFailure(Call<List<TournamentDTO>> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void getTournaments(Integer count) {
        App.getAPI().getTournaments().enqueue(new Callback<List<TournamentDTO>>() {
            @Override
            public void onResponse(Call<List<TournamentDTO>> call, Response<List<TournamentDTO>> response) {
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
            public void onFailure(Call<List<TournamentDTO>> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void searchTournaments(String text) {
        App.getAPI().searchTournaments(text).enqueue(new Callback<List<TournamentDTO>>() {
            @Override
            public void onResponse(Call<List<TournamentDTO>> call, Response<List<TournamentDTO>> response) {
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
            public void onFailure(Call<List<TournamentDTO>> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void attachPresenter(CallBackSearchTournament callBack) {
        this.callBack = callBack;
    }
}
