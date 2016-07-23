package net.controly.controly.http.service;

import net.controly.controly.http.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * REST API for login requests.
 */
public interface LoginService {

    /**
     * Login a user according to the given username/email & password.
     */
    @FormUrlEncoded
    @POST("verifyUserPass")
    Call<LoginResponse> login(@Field("userOrEmail") String userOrEmail, @Field("pass") String password);

}
