
package com.supermandelivery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavedAddress {

    @SerializedName("geopoint")
    @Expose
    private Geopoint geopoint;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("flatNo")
    @Expose
    private String flatNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * 
     * @return
     *     The geopoint
     */
    public Geopoint getGeopoint() {
        return geopoint;
    }

    /**
     * 
     * @param geopoint
     *     The geopoint
     */
    public void setGeopoint(Geopoint geopoint) {
        this.geopoint = geopoint;
    }

    /**
     * 
     * @return
     *     The remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 
     * @param remarks
     *     The remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The flatNo
     */
    public String getFlatNo() {
        return flatNo;
    }

    /**
     * 
     * @param flatNo
     *     The flatNo
     */
    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The contactNo
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * 
     * @param contactNo
     *     The contactNo
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SavedAddress{" +
                "geopoint=" + geopoint +
                ", remarks='" + remarks + '\'' +
                ", address='" + address + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", name='" + name + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
