package bobrchess.of.by.belaruschess.network.connection;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 11.04.2018.
 */

public class UserConnection {

    private CallBackUser callBack;

    public void authorizate(String email, String password) {
        App.getAPI().authorizate(email,password).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void registrate(String email, String password) {
        App.getAPI().authorizate(email,password).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }


    public void attachView(CallBackUser callBack) {
        this.callBack = callBack;
    }
}
