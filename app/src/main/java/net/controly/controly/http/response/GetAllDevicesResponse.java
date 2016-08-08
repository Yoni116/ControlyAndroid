package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Device;

/**
 * Created by Itai on 08-Aug-16.
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
