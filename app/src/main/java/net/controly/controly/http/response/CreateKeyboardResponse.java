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

    public CreateKeyboardResponse() {
    }

    public String getKeyboardPicture() {
        return keyboardPicture;
    }

    public void setKeyboardPicture(String keyboardPicture) {
        this.keyboardPicture = keyboardPicture;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getKeyboardId() {
        return keyboardId;
    }

    public void setKeyboardId(int keyboardId) {
        this.keyboardId = keyboardId;
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
