
package com.supermandelivery.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripsResponseModel {

    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("pickup_time")
    @Expose
    private String pickupTime;
    @SerializedName("scheduledTime")
    @Expose
    private String scheduledTime;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("discount")
    @Expose
    private Discount discount;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("net")
    @Expose
    private double net;
    @SerializedName("totalFare")
    @Expose
    private double totalFare;
    @SerializedName("waitingTime")
    @Expose
    private double waitingTime;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("perKmCost")
    @Expose
    private double perKmCost;
    @SerializedName("waitingCharge")
    @Expose
    private double waitingCharge;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("minFare")
    @Expose
    private double minFare;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("_pickupAddress")
    @Expose
    private com.supermandelivery.models.PickupAddress PickupAddress;
    @SerializedName("_dropAddress")
    @Expose
    private com.supermandelivery.models.DropAddress DropAddress;
    @SerializedName("_purchaseAddress")
    @Expose
    private com.supermandelivery.models.PurchaseAddress PurchaseAddress;

    /**
     * @return The rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks The remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return The pickupTime
     */
    public String getPickupTime() {
        return pickupTime;
    }

    /**
     * @param pickupTime The pickup_time
     */
    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    /**
     * @return The scheduledTime
     */
    public String getScheduledTime() {
        return scheduledTime;
    }

    /**
     * @param scheduledTime The scheduledTime
     */
    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    /**
     * @return The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return The discount
     */
    public Discount getDiscount() {
        return discount;
    }

    /**
     * @param discount The discount
     */
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    /**
     * @return The couponCode
     */
    public String getCouponCode() {
        return couponCode;
    }

    /**
     * @param couponCode The couponCode
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    /**
     * @return The net
     */
    public double getNet() {
        return net;
    }

    /**
     * @param net The net
     */
    public void setNet(double net) {
        this.net = net;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    /**
     * @return The waitingTime
     */
    public double getWaitingTime() {
        return waitingTime;
    }

    /**
     * @param waitingTime The waitingTime
     */
    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    /**
     * @return The distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return The perKmCost
     */
    public double getPerKmCost() {
        return perKmCost;
    }

    /**
     * @param perKmCost The perKmCost
     */
    public void setPerKmCost(double perKmCost) {
        this.perKmCost = perKmCost;
    }

    /**
     * @return The waitingCharge
     */
    public double getWaitingCharge() {
        return waitingCharge;
    }

    /**
     * @param waitingCharge The waitingCharge
     */
    public void setWaitingCharge(double waitingCharge) {
        this.waitingCharge = waitingCharge;
    }

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
     * @return The minFare
     */
    public double getMinFare() {
        return minFare;
    }

    /**
     * @param minFare The minFare
     */
    public void setMinFare(double minFare) {
        this.minFare = minFare;
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

    /**
     * @return The couponId
     */
    public String getCouponId() {
        return couponId;
    }

    /**
     * @param couponId The coupon_id
     */
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    /**
     * @return The PickupAddress
     */
    public com.supermandelivery.models.PickupAddress getPickupAddress() {
        return PickupAddress;
    }

    /**
     * @param PickupAddress The _pickupAddress
     */
    public void setPickupAddress(com.supermandelivery.models.PickupAddress PickupAddress) {
        this.PickupAddress = PickupAddress;
    }

    /**
     * @return The DropAddress
     */
    public com.supermandelivery.models.DropAddress getDropAddress() {
        return DropAddress;
    }

    /**
     * @param DropAddress The _dropAddress
     */
    public void setDropAddress(com.supermandelivery.models.DropAddress DropAddress) {
        this.DropAddress = DropAddress;
    }

    /**
     * @return The PurchaseAddress
     */
    public com.supermandelivery.models.PurchaseAddress getPurchaseAddress() {
        return PurchaseAddress;
    }

    /**
     * @param PurchaseAddress The _purchaseAddress
     */
    public void setPurchaseAddress(com.supermandelivery.models.PurchaseAddress PurchaseAddress) {
        this.PurchaseAddress = PurchaseAddress;
    }

    @Override
    public String toString() {
        return "TripsResponseModel{" +
                "rating=" + rating +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", remarks='" + remarks + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", scheduledTime='" + scheduledTime + '\'' +
                ", productId='" + productId + '\'' +
                ", discount=" + discount +
                ", couponCode='" + couponCode + '\'' +
                ", net=" + net +
                ", totalFare=" + totalFare +
                ", waitingTime=" + waitingTime +
                ", distance=" + distance +
                ", perKmCost=" + perKmCost +
                ", waitingCharge=" + waitingCharge +
                ", endTime='" + endTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", minFare=" + minFare +
                ", id='" + id + '\'' +
                ", couponId='" + couponId + '\'' +
                ", PickupAddress=" + PickupAddress +
                ", DropAddress=" + DropAddress +
                ", PurchaseAddress=" + PurchaseAddress +
                '}';
    }
}
