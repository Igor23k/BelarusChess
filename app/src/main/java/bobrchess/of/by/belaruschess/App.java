package bobrchess.of.by.belaruschess;

import android.app.Application;

import bobrchess.of.by.belaruschess.network.api.API;
import bobrchess.of.by.belaruschess.util.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Igor on 10.04.2018.
 */

public class App extends Application {

    private static API api;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public static API getAPI() {
        return api;
    }
}
