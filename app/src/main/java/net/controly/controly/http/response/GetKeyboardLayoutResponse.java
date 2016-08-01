package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.KeysLayout;
import net.controly.controly.util.GsonFactory;

/**
 * This class represents a response for getting a keyboard layout.
 */
public class GetKeyboardLayoutResponse extends BaseResponse {

    private Data[] data;

    public GetKeyboardLayoutResponse() {
    }

    public KeysLayout getKeysLayout() {
        if(data[0] == null) {
            return null;
        }

        return data[0].getKeysLayout();
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    /**
     * This class encapsulates the data layout of the keyboard.
     */
    public class Data {

        @SerializedName("KeysLayout")
        private String keysLayout;

        public Data() {
        }

        public KeysLayout getKeysLayout() {
            return GsonFactory.getGson()
                    .fromJson(keysLayout, KeysLayout.class);
        }

        public void setKeysLayout(String keysLayout) {
            this.keysLayout = keysLayout;
        }
    }
}
