package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Itai on 16-Sep-16.
 */
public class UserKey extends Key {

    @SerializedName("KeyID")
    private long id;

    @SerializedName("KeyName")
    private String name;

    @SerializedName("DeviceIDs")
    private long device;

    public UserKey(String command) {
        super(command);
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

    public long getDevices() {
        return device;
    }

    public void setDevices(long devices) {
        this.device = devices;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
