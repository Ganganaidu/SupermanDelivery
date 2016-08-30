package com.supermandelivery.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ganga on 3/16/2016.
 */
public class OtpResponse {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("_savedAddresses")
    @Expose
    private List<Object> SavedAddresses = new ArrayList<Object>();
    @SerializedName("updated_on")
    @Expose
    private String updatedOn;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("success")
    @Expose
    private Boolean success;

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @return The SavedAddresses
     */
    public List<Object> getSavedAddresses() {
        return SavedAddresses;
    }

    /**
     * @param SavedAddresses The _savedAddresses
     */
    public void setSavedAddresses(List<Object> SavedAddresses) {
        this.SavedAddresses = SavedAddresses;
    }

    /**
     * @return The updatedOn
     */
    public String getUpdatedOn() {
        return updatedOn;
    }

    /**
     * @param updatedOn The updated_on
     */
    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
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

    /**
     * @return The otp
     */
    public String getOtp() {
        return otp;
    }

    /**
     * @param otp The otp
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * @return The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
