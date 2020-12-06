package bobrchess.of.by.belaruschess;

import android.app.Application;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
                .client(getUnsafeOkHttpClient().build())
                .build();
        externalFideServerApi = retrofitExternalFideServerApi.create(ExternalFideServerApi.class);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {//todo should be fixed https://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PersonalServerApi getPersonalServerApi() {
        return personalServerApi;
    }

    public static ExternalFideServerApi getExternalFideServerApi() {
        return externalFideServerApi;
    }
}
