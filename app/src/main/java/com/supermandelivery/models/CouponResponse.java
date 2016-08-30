package com.supermandelivery.models;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class CouponResponse {

    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("percentage")
    @Expose
    private Integer percentage;
    @SerializedName("discountAmount")
    @Expose
    private Integer discountAmount;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("maxAmount")
    @Expose
    private Integer maxAmount;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    /**
     *
     * @return
     * The couponId
     */
    public String getCouponId() {
        return couponId;
    }

    /**
     *
     * @param couponId
     * The coupon_id
     */
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    /**
     *
     * @return
     * The percentage
     */
    public Integer getPercentage() {
        return percentage;
    }

    /**
     *
     * @param percentage
     * The percentage
     */
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    /**
     *
     * @return
     * The discountAmount
     */
    public Integer getDiscountAmount() {
        return discountAmount;
    }

    /**
     *
     * @param discountAmount
     * The discountAmount
     */
    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     *
     * @return
     * The expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     *
     * @param expiryDate
     * The expiryDate
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     *
     * @return
     * The quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     * The quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The maxAmount
     */
    public Integer getMaxAmount() {
        return maxAmount;
    }

    /**
     *
     * @param maxAmount
     * The maxAmount
     */
    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     * The created_on
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}