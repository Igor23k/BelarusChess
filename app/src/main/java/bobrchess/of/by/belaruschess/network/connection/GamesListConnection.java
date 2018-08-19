package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGamesList;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE;
import static bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST;

/**
 * Created by Igor on 11.04.2018.
 */

public class GamesListConnection {

    private CallBackGamesList callBack;

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

    public void getGames(Integer count) {
        App.getAPI().getGames(count).enqueue(new Callback<List<GameDTO>>() {
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
                callBack.onFailure(t);
            }
        });
    }

    public void searchGames(String text) {
        App.getAPI().searchGames(text).enqueue(new Callback<List<GameDTO>>() {
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
                callBack.onFailure(t);
            }
        });
    }

    public void attachPresenter(CallBackGamesList callBack) {
        this.callBack = callBack;
    }
}
