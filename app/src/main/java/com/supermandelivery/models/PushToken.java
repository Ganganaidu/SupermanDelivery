package com.supermandelivery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushToken {

    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;

    /**
     * @return The appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId The appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return The deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * @param deviceToken The deviceToken
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType The deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

}
