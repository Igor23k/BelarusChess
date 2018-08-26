package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE;
import static bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST;

/**
 * Created by Igor on 11.04.2018.
 */

public class RegistrationConnection {

    private CallBackRegistration callBack;

    public void registration(UserDTO userDTO) {
        App.getAPI().registration(userDTO).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
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
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void getCoaches() {
        App.getAPI().getUsers().enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack.onCoachResponse(response.body());
                    } else {
                        callBack.onFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void getRanks() {
        App.getAPI().getRanks().enqueue(new Callback<List<RankDTO>>() {
            @Override
            public void onResponse(Call<List<RankDTO>> call, Response<List<RankDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack.onRankResponse(response.body());
                    } else {
                        callBack.onFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<List<RankDTO>> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void getCountries() {
        App.getAPI().getCountries().enqueue(new Callback<List<CountryDTO>>() {
            @Override
            public void onResponse(Call<List<CountryDTO>> call, Response<List<CountryDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack.onCountryResponse(response.body());
                    } else {
                        callBack.onFailure(new Throwable(response.raw().header(ERROR_PARAMETER)));
                    }
                } else {
                    callBack.onFailure(new Throwable(UNSUCCESSFUL_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<List<CountryDTO>> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void attachPresenter(CallBackRegistration callBack) {
        this.callBack = callBack;
    }
}
