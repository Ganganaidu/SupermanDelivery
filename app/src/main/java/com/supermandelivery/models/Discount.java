
package com.supermandelivery.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Discount {

    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("percentage")
    @Expose
    private double percentage;
    @SerializedName("discountAmount")
    @Expose
    private double discountAmount;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("quantity")
    @Expose
    private double quantity;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("maxAmount")
    @Expose
    private String maxAmount;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
     * @return The percentage
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * @param percentage The percentage
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * @return The discountAmount
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * @param discountAmount The discountAmount
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * @return The expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate The expiryDate
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return The quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity The quantity
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
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
     * @return The status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return The amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return The maxAmount
     */
    public String getMaxAmount() {
        return maxAmount;
    }

    /**
     * @param maxAmount The maxAmount
     */
    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn The created_on
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
