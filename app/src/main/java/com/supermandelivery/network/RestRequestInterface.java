package com.supermandelivery.network;

import com.supermandelivery.models.Discount;
import com.supermandelivery.models.Fare;
import com.supermandelivery.models.LoginResponse;
import com.supermandelivery.models.OtpResponse;
import com.supermandelivery.models.Prodcut;
import com.supermandelivery.models.PushToken;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.models.TripsResponseModel;
import com.supermandelivery.models.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Created by Office on 3/16/2016.
 */
public interface RestRequestInterface {

    /*OTP call*/
    @FormUrlEncoded
    @POST("users/getotp")
    Call<OtpResponse> createUserOtpCall(@Field("username") String username);

    /*Loin and access token call*/
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> getAccessToken(@Field("username") String username, @Field("password") String password);

    //access_token
    @PUT("users/{id}")
    Call<UserProfile> updateProfile(@Header("Authorization") String authorization, @Body UserProfile user, @Path("id") String id);

    @GET("users/{id}/savedAddresses")
    Call<List<SavedAddress>> getSavedAddress(@Header("Authorization") String authorization, @Path("id") String userID);

    @POST("users/{id}/savedAddresses")
    Call<SavedAddress> addAddress(@Header("Authorization") String authorization, @Body SavedAddress savedAddress, @Path("id") String id);

    @DELETE("users/{id}/savedAddresses/{fk}")
    Call<String> deleteAddress(@Header("Authorization") String authorization, @Path("id") String userID, @Path("fk") String addressID);

    @GET("products")
    Call<List<Prodcut>> getProducts(@Header("Authorization") String authorization);

    @GET("fares")
    Call<List<Fare>> getFareChart(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("trips/validateCoupon")
    Call<Discount> validateCoupon(@Header("Authorization") String authorization, @Field("couponCode") String couponCode, @Field("net") String net, @Field("user_id") String user_id);

    @GET("users/{id}/trips")
    Call<List<TripsModel>> getTrips(@Header("Authorization") String authorization, @Path("id") String userID);

    @POST("trips")
    Call<TripsResponseModel> postTrips(@Header("Authorization") String authorization, @Body TripsModel tripsModel);

    //push token
    @PUT("registrations")
    Call<PushToken> sendGCMPushToken(@Header("Authorization") String authorization, @Body PushToken pushToken);
}




