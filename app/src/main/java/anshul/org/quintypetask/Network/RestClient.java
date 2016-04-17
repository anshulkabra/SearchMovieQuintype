package anshul.org.quintypetask.Network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class RestClient {

    public static final String API_BASE_URL = "http://www.omdbapi.com/";
    public static ApiService service;
    public static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    static OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();

    public static ApiService getApiService() {

        Retrofit retrofit = builder.client(defaultHttpClient.cache(null).connectTimeout(120, TimeUnit.SECONDS).build()).build();

        return service = retrofit.create(ApiService.class);
    }

}
