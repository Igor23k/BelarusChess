package bobrchess.of.by.belaruschess;

import android.app.Application;

import java.io.IOException;

import bobrchess.of.by.belaruschess.network.api.API;
import bobrchess.of.by.belaruschess.util.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Cache-Control", "no-cache")
                    .build();
            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Companion.getHOST())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(API.class);
    }

    public static API getAPI() {
        return api;
    }
}
