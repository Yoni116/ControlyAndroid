package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a device action.
 */
public class DeviceKey extends Key {

    @SerializedName("DeviceKeyID")
    private long id;

    @SerializedName("DeviceID")
    private long deviceId;

    @SerializedName("Description")
    private String description;

    public DeviceKey(String command) {
        super(command);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public String getDeviceIdFormatted() {
        return "dk" + id;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getTitle() {
        return description;
    }
}
