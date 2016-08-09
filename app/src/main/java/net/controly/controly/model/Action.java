package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a device action.
 */
public class Action {

    @SerializedName("DeviceKeyID")
    private long id;

    @SerializedName("DeviceID")
    private long deviceId;

    @SerializedName("Description")
    private String description;

    @SerializedName("Command")
    private String command;

    public Action() {
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

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
