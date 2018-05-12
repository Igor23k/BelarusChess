package bobrchess.of.by.belaruschess.network.connection;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackTournament;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    callBack.onResponse(response.body());
                } else {
                    //тут нужно блок экрана снимать и тд
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
                callBack.onFailure(t);
            }
        });
    }


    public void attachPresenter(CallBackTournament callBack) {
        this.callBack = callBack;
    }
}
