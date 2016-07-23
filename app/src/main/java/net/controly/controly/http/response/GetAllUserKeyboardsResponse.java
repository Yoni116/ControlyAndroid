package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Keyboard;

import java.util.List;

/**
 * This class represents a response from the server for getting user keyboards.
 */
public class GetAllUserKeyboardsResponse extends BaseResponse {

    @SerializedName("data")
    private List<Keyboard> keyboards;

    public List<Keyboard> getKeyboards() {
        return keyboards;
    }

    public void setKeyboards(List<Keyboard> keyboards) {
        this.keyboards = keyboards;
    }

    @Override
    public String toString() {
        if (hasSucceeded()) {
            return "GetAllUserKeyboardsResponse{" +
                    "keyboards='" + keyboards + '\'' +
                    '}';
        }

        return "GetAllUserKeyboardsResponse{" +
                "status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
