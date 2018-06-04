package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import bobrchess.of.by.belaruschess.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
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
                        callBack.onAddTournamentFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onResponse(Util.getTestTournament());
                   // callBack.onAddTournamentFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<TournamentDTO> call, Throwable t) {
                callBack.onConnectionError(t);
            }
        });
    }

    public void attachPresenter(CallBackAddTournament callBack) {
        this.callBack = callBack;
    }
}
