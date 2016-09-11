package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

/**
 * This is a response class for creating a new keyboard.
 */
public class CreateKeyboardResponse extends BaseResponse {

    @SerializedName("keyboardPic")
    private String keyboardPicture;
    private String backgroundImage;
    private int keyboardId;

    public CreateKeyboardResponse(String keyboardPicture, String backgroundImage, int keyboardId) {
        this.keyboardPicture = keyboardPicture;
        this.backgroundImage = backgroundImage;
        this.keyboardId = keyboardId;
    }

    public String getKeyboardPicture() {
        return keyboardPicture;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public int getKeyboardId() {
        return keyboardId;
    }

    @Override
    public String toString() {

        //Return the relevant fields according to whether the login succeeded or failed.
        if (hasSucceeded()) {
            return "CreateKeyboardResponse{" +
                    "keyboardPicture='" + keyboardPicture + '\'' +
                    ", backgroundImage='" + backgroundImage + '\'' +
                    ", keyboardId=" + keyboardId +
                    '}';
        }

        return "CreateKeyboardResponse{" +
                "status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
