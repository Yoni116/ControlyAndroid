package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * This class represents a device that can perform actions and that can have controllers in the app.
 */
public class Device implements BoxListItem, Serializable {

    @SerializedName("DeviceID")
    private long id;

    @SerializedName("DeviceName")
    private String name;

    @SerializedName("DevicePic")
    private String picture;

    @SerializedName("Category")
    private String category;

    @SerializedName("Protocol")
    private String protocol;

    public Device() {
    }

    public Device(long id, String name, String picture, String category, String protocol) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.category = category;
        this.protocol = protocol;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
