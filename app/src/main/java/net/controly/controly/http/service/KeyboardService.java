package net.controly.controly.http.service;

import net.controly.controly.http.response.CreateKeyboardResponse;
import net.controly.controly.http.response.DeleteKeyboardResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * REST API for keyboard operations
 */
public interface KeyboardService {

    @POST("deleteKeyboard")
    @FormUrlEncoded
    Call<DeleteKeyboardResponse> deleteKeyboard(@Field("userId") long userId, @Field("keyboardId") long keyboardId);

    @Multipart
    @POST("createKeyboard")
    Call<CreateKeyboardResponse> createKeyboard(@Part("keyboardPic") RequestBody keyboardPicture, @Part("keyboardName") String keyboardName);
}
