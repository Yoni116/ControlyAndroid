package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Action;

/**
 * This class represents a response from the API for getting the actions that a certain device can perform.
 */
public class GetActionsForDeviceResponse extends BaseResponse {

    @SerializedName("data")
    private Action[] actions;

    public GetActionsForDeviceResponse() {
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }
}
