package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.User;

import java.util.Map;

/**
 * This class represents a response from the server for logging in to the application.
 */
public class LoginResponse extends BaseResponse {

    private Map<String, String> data;
    private String jwt;

    @SerializedName("ID")
    private int id;

    public LoginResponse() {
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getData() {
        return data;
    }

    public User getUser() {
        return data == null ? null : new User(data);
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {

        //Return the relevant fields according to whether the login succeeded or failed.
        if (hasSucceeded()) {
            return "LoginResponse{" +
                    "status='" + status + '\'' +
                    ", data=" + data +
                    ", jwt='" + jwt + '\'' +
                    '}';
        }

        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", ID='" + id + '\'' +
                '}';
    }
}
