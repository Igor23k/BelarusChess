package bobrchess.of.by.belaruschess.network;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.presenter.CallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 11.04.2018.
 */

public class Network {

    private CallBack callBack;

    public void getCountry() {
        App.getAPI().getCountry().enqueue(new Callback<CountryDTO>() {
            @Override
            public void onResponse(Call<CountryDTO> call, Response<CountryDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<CountryDTO> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void attachView(CallBack callBack) {
        this.callBack = callBack;
    }
}
