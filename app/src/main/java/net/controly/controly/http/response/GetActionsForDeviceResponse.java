package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.DeviceKey;

/**
 * This class represents a response from the API for getting the deviceKeys that a certain device can perform.
 */
public class GetActionsForDeviceResponse extends BaseResponse {

    @SerializedName("data")
    private DeviceKey[] deviceKeys;

    public GetActionsForDeviceResponse() {
    }

    public DeviceKey[] getDeviceKeys() {
        return deviceKeys;
    }

    public void setDeviceKeys(DeviceKey[] deviceKeys) {
        this.deviceKeys = deviceKeys;
    }
}
