package bobrchess.of.by.belaruschess.network.connection;

import org.apache.commons.httpclient.HttpStatus;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackCountry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bobrchess.of.by.belaruschess.util.Constants.ERROR_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE;
import static bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST;

/**
 * Created by Igor on 11.04.2018.
 */

public class CountryConnection {

    private CallBackCountry callBack;

    public void getCountry(Integer id) {
        App.getAPI().getCountry(id).enqueue(new Callback<CountryDTO>() {
            @Override
            public void onResponse(Call<CountryDTO> call, Response<CountryDTO> response) {
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
            public void onFailure(Call<CountryDTO> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }

    public void addCountry(CountryDTO countryDTO) {
        App.getAPI().addCountry(countryDTO).enqueue(new Callback<CountryDTO>() {
            @Override
            public void onResponse(Call<CountryDTO> call, Response<CountryDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                } else {
                    callBack.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(Call<CountryDTO> call, Throwable t) {
                callBack.onFailure(new Throwable(SERVER_UNAVAILABLE));
            }
        });
    }


    public void attachPresenter(CallBackCountry callBack) {
        this.callBack = callBack;
    }
}
