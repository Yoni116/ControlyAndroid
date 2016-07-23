package net.controly.controly.http.service;

import net.controly.controly.http.response.GetAllUserKeyboardsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * REST API for user requests.
 */
public interface UserService {

    /**
     * Get a list of keyboards that the given user possess.
     *
     * @param jwt    The token of the authenticated user.
     * @param userId The user to get it's keyboard list.
     * @return The keyboard list of the given user.
     */
    @FormUrlEncoded
    @POST("getAllUserKeyboards")
    Call<GetAllUserKeyboardsResponse> getAllUserKeyboards(@Header("Authorization") String jwt, @Field("userId") long userId);

}
