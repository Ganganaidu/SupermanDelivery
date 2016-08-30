package com.supermandelivery.network;

import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.model.LatLng;
import com.supermandelivery.listeners.OnDirectionCallback;
import com.supermandelivery.listeners.RequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ganga on 3/17/2016.
 */
public class RestAPIRequest {

    private static final String TAG = "RestAPIRequest";

//    public static String BASE_URL = "http://52.36.148.14:3005/api/";

    //public static String BASE_URL = "http://52.36.121.72:3005/api/";

    public static String BASE_URL = "http://52.77.125.136:3005/api/"; //Production URL

    private static Retrofit retrofit;
    private static RestAPIRequest instance;

    public static RestAPIRequest getInstance() {
        if (instance == null) {
            instance = new RestAPIRequest();
        }
        return instance;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /*Generic method for doing all API requests in the application*/
    public <T> void doRequest(Call<T> call, final RequestListener<T> requestListener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.e(TAG, "response code:" + response.code());

                if (response.code() == 200 || response.code() == 204) {
                    requestListener.onResponse(response.body());
                    //Log.e(TAG, "response:" + response.body());
                } else {
                    requestListener.onDisplayError(response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                requestListener.onDisplayError(t.getMessage());
                Log.e(TAG, "errorMsg:" + t.getMessage());
            }
        });
    }

    public void getDirectionGoogleAPI(String apiKey, LatLng origin, LatLng destination, final OnDirectionCallback callback) {
        // LatLng origin = new LatLng(37.7849569, -122.4068855);
        // LatLng destination = new LatLng(37.7814432, -122.4460177);
        GoogleDirection.withServerKey(apiKey)
                .from(origin)
                .to(destination)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        // Do something here
                        callback.onDirectionSuccess(direction, rawBody);
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                        callback.onDirectionFailure(t);
                    }
                });
    }
}
