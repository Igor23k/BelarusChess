package bobrchess.of.by.belaruschess.network.connection;

import android.renderscript.Int3;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackCountry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    callBack.onResponse(response.body());
                }else {
                    //тут нужно блок экрана снимать и тд
                }
            }

            @Override
            public void onFailure(Call<CountryDTO> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }


    public void attachView(CallBackCountry callBack) {
        this.callBack = callBack;
    }
}
