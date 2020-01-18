package buildnlive.com.buildlive.Server;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiAuthSingleTon {

    private static Retrofit retrofitAuth;
    private static Retrofit retrofit;
    private static OkHttpClient client;

    private static Retrofit createRetrofitAuth(String authToken, String uuid) {

        if (authToken != (null)) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();


            okHttpBuilder.addInterceptor(loggingInterceptor);

            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken, uuid);

            okHttpBuilder.addInterceptor(interceptor);

            client = okHttpBuilder.build();

            retrofitAuth = new Retrofit.Builder().baseUrl("https://buildnlive.com/app/mobileapp/")
                    .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

        }
        return retrofitAuth;
    }

    private static Retrofit createRetrofit() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();


        okHttpBuilder.addInterceptor(loggingInterceptor);

        client = okHttpBuilder.build();

        retrofit = new Retrofit.Builder().baseUrl("https://buildnlive.com/app/mobileapp/")
                .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            return createRetrofit().create(serviceClass);
        } else {
            return retrofit.create(serviceClass);
        }

    }

    public static <S> S createAuthService(Class<S> serviceClass, String auth, String uuid) {
        if (retrofitAuth == null) {
            return createRetrofitAuth(auth, uuid).create(serviceClass);
        } else {
            return retrofitAuth.create(serviceClass);
        }

    }



    public static void clearRetrofit() {
        retrofit = null;
        retrofitAuth = null;
    }

}



/*package com.webpulse.noor.Server;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitApiAuthSingleTon {

    private static Retrofit retrofit;
    private static OkHttpClient client;

    private static Retrofit createRetrofitAuth(String authToken,String uuid) {
        if (authToken != null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();


            okHttpBuilder.addInterceptor(loggingInterceptor);

            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken,uuid);

            okHttpBuilder.addInterceptor(interceptor);

            client = okHttpBuilder.build();

            retrofit= new Retrofit.Builder().baseUrl("https://www.webpulse.co/webpulseApp/web_apis/")
                    .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass,String auth,String uuid) {
        if ((retrofit == null))
            return createRetrofitAuth(auth,uuid).create(serviceClass);
        else
            return retrofit.create(serviceClass);
    }

    public static void clearRetrofit() {
        retrofit = null;
    }

    //in any case where the current retrofit instance is required, call this method
    public static Retrofit getRetrofit(String auth,String uuid) {
        if (retrofit == null)
            return createRetrofitAuth(auth,uuid);
        else
            return retrofit;
    }

}*/

