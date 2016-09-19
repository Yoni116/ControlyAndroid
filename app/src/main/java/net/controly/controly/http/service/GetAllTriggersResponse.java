package net.controly.controly.http.service;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.http.response.BaseResponse;
import net.controly.controly.model.Trigger;

/**
 * This class represents a response from the API for getting all triggers.
 */
public class GetAllTriggersResponse extends BaseResponse {

    @SerializedName("data")
    private final Trigger[] triggers;

    public GetAllTriggersResponse(Trigger[] triggers) {
        this.triggers = triggers;
    }

    public Trigger[] getTriggers() {
        return triggers;
    }
}
