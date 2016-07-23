package net.controly.controly.http.service;

import net.controly.controly.http.response.GetAllUserKeyboardsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * REST API for user requests.
 */
public interface UserService {

    /**
     * Get a list of keyboards that the given user possess.
     *
     * @param userId The user to get it's keyboard list.
     * @return The keyboard list of the given user.
     */
    @FormUrlEncoded
    @POST("getAllUserKeyboards")
    Call<GetAllUserKeyboardsResponse> getAllUserKeyboards(@Field("userId") long userId);

}
