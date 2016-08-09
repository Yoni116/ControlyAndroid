package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Device;

/**
 * This is class represents a response from the API for receiving all of the devices that the platform integrates with.
 */
public class GetAllDevicesResponse extends BaseResponse {

    @SerializedName("data")
    private Device[] devices;

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }
}
