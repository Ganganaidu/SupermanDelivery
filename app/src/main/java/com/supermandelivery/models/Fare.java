package com.supermandelivery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Office on 3/19/2016.
 */
public class Fare {

    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("minDistance")
    @Expose
    private Integer minDistance;
    @SerializedName("minFare")
    @Expose
    private Integer minFare;
    @SerializedName("perKmCost")
    @Expose
    private Integer perKmCost;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("waitingCharge")
    @Expose
    private Integer waitingCharge;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * @return The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return The minDistance
     */
    public Integer getMinDistance() {
        return minDistance;
    }

    /**
     * @param minDistance The minDistance
     */
    public void setMinDistance(Integer minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * @return The minFare
     */
    public Integer getMinFare() {
        return minFare;
    }

    /**
     * @param minFare The minFare
     */
    public void setMinFare(Integer minFare) {
        this.minFare = minFare;
    }

    /**
     * @return The perKmCost
     */
    public Integer getPerKmCost() {
        return perKmCost;
    }

    /**
     * @param perKmCost The perKmCost
     */
    public void setPerKmCost(Integer perKmCost) {
        this.perKmCost = perKmCost;
    }

    /**
     * @return The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The waitingCharge
     */
    public Integer getWaitingCharge() {
        return waitingCharge;
    }

    /**
     * @param waitingCharge The waitingCharge
     */
    public void setWaitingCharge(Integer waitingCharge) {
        this.waitingCharge = waitingCharge;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

}