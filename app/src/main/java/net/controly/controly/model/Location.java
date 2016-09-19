package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a location that an event takes place at.
 */
public class Location implements BoxListItem, Serializable {

    @SerializedName("LocationID")
    private final long id;

    @SerializedName("UserID")
    private final long userId;

    @SerializedName("Latitude")
    private final double latitude;

    @SerializedName("Longitude")
    private final double longitude;

    @SerializedName("Description")
    private final String description;

    @SerializedName("CreatedAt")
    private final Date creationDate;

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
