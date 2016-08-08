package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Action;

/**
 * Created by Itai on 08-Aug-16.
 */
public class GetKeysForDeviceResponse extends BaseResponse {

    @SerializedName("data")
    private Action[] actions;

    public GetKeysForDeviceResponse() {
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }
}
