package bobrchess.of.by.belaruschess.network.connection;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGame;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 11.04.2018.
 */

public class GameConnection {

    private CallBackGame callBack;

    public void getGame(Integer id) {
        App.getAPI().getGame(id).enqueue(new Callback<GameDTO>() {
            @Override
            public void onResponse(Call<GameDTO> call, Response<GameDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                } else {
                    //тут нужно блок экрана снимать и тд
                }
            }

            @Override
            public void onFailure(Call<GameDTO> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void getGames() {
        App.getAPI().getGames().enqueue(new Callback<List<GameDTO>>() {
            @Override
            public void onResponse(Call<List<GameDTO>> call, Response<List<GameDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                } else {
                    //тут нужно блок экрана снимать и тд
                }
            }

            @Override
            public void onFailure(Call<List<GameDTO>> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }


    public void attachPresenter(CallBackGame callBack) {
        this.callBack = callBack;
    }
}
