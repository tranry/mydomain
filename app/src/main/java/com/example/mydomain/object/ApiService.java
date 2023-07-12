package com.example.mydomain.object;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    Interceptor inter = new Interceptor() {
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Content-Type", "application/json, text/plain, */*");
            builder.addHeader("X-Device", "2c573722-435b-43c9-a38b-5687db79d6fd");
            builder.addHeader("X-App-Key", "vH6_jQdC6FZ_84rDhT!xo*C2jFdFg7jK");
            builder.addHeader("X-App-Version", "3.2.8");
            builder.addHeader("X-App-Bundle", "com.furkankavlak.whois");
            builder.addHeader("User-Agent", "Domain%20Search/47 CFNetwork/1121.2.2 Darwin/19.2.0");
            return chain.proceed(builder.build());
        }
    };

    OkHttpClient.Builder http = new OkHttpClient.Builder().addInterceptor(inter);

    ApiService api = new Retrofit.Builder().baseUrl("https://api.furkankavlak.com/")
            .addConverterFactory(GsonConverterFactory.create()).client(http.build()).build().create(ApiService.class);

    @GET("apps/whois")
    Call<DataObject> call(@Query("domain") String domain);

}
