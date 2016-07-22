package net.controly.controly.http.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * REST API for login requests.
 */
public interface LoginService {

    /**
     * Login a user.
     */

    @FormUrlEncoded
    @POST("verifyUserPass")
    Call<ResponseBody> login(@Field("userOrEmail") String userOrEmail, @Field("pass") String password);

}
