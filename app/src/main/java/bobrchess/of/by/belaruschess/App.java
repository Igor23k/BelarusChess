package bobrchess.of.by.belaruschess;

import android.app.Application;

import java.io.ByteArrayInputStream;

import bobrchess.of.by.belaruschess.network.api.ExternalFideServerApi;
import bobrchess.of.by.belaruschess.network.api.PersonalServerApi;
import bobrchess.of.by.belaruschess.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static PersonalServerApi personalServerApi;
    private static ExternalFideServerApi externalFideServerApi;
    private Retrofit retrofitPersonalServerApi;
    private Retrofit retrofitExternalFideServerApi;

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
        retrofitPersonalServerApi = new Retrofit.Builder()
                .baseUrl(Constants.Companion.getPERSONAL_SERVER_HOST())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        personalServerApi = retrofitPersonalServerApi.create(PersonalServerApi.class);

        retrofitExternalFideServerApi = new Retrofit.Builder()
                .baseUrl(Constants.Companion.getEXTERNAL_FIDE_API_SERVER_HOST())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        externalFideServerApi = retrofitExternalFideServerApi.create(ExternalFideServerApi.class);
    }

    public static PersonalServerApi getPersonalServerApi() {
        return personalServerApi;
    }

    public static ExternalFideServerApi getExternalFideServerApi() {
        return externalFideServerApi;
    }
}
