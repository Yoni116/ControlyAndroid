package net.controly.controly.http.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Keyboard;
import net.controly.controly.util.GsonFactory;

/**
 * This class represents a response from the API for receiving a keyboard by its id.
 */
public class GetKeyboardByIdResponse extends BaseResponse {

    @SerializedName("data")
    private JsonElement keyboard;

    public GetKeyboardByIdResponse(JsonElement keyboard) {
        this.keyboard = keyboard;
    }

    public Keyboard getKeyboard() {
        return GsonFactory.getGson()
                .fromJson(keyboard, Keyboard[].class)[0];
    }
}
