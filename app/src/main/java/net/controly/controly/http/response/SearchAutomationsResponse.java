package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.http.response.BaseResponse;
import net.controly.controly.model.UserKey;

/**
 * Created by Itai on 16-Sep-16.
 */
public class SearchAutomationsResponse extends BaseResponse {

    @SerializedName("data")
    private final UserKey[] automations;

    public SearchAutomationsResponse(UserKey[] automations) {
        this.automations = automations;
    }

    public UserKey[] getAutomations() {
        return automations;
    }
}
