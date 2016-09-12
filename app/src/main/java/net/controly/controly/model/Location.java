package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a location that an event takes place at.
 */
public class Location implements BoxListItem, Serializable {

    @SerializedName("LocationID")
    private long id;

    @SerializedName("UserID")
    private long userId;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Description")
    private String description;

    @SerializedName("CreatedAt")
    private Date creationDate;

    public Location(long id, long userId, double latitude, double longitude, String description, Date creationDate) {
        this.id = id;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.creationDate = creationDate;
    }

    @Override
    public String getTitle() {
        return description;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
