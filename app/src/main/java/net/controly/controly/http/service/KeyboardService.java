package net.controly.controly.http.service;

import net.controly.controly.http.response.BaseResponse;
import net.controly.controly.http.response.CreateKeyboardResponse;
import net.controly.controly.http.response.GetAllDevicesResponse;
import net.controly.controly.http.response.GetKeyboardByIdResponse;
import net.controly.controly.http.response.GetKeyboardLayoutResponse;
import net.controly.controly.http.response.GetActionsForDeviceResponse;
import net.controly.controly.model.KeysLayout;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<BaseResponse> deleteKeyboard(@Field("userId") long userId, @Field("keyboardId") long keyboardId);

    @Multipart
    @POST("createKeyboard")
    Call<CreateKeyboardResponse> createKeyboard(@Part("userId") long userId, @Part("keyboardName") String keyboardName,
                                                @Part("hashTag") String hashTag, @Part("portrait") int portrait,
                                                @Part("screenSize") String screenSize, @Part("hasPic") int hasPic,
                                                @Part MultipartBody.Part keyboardPic, @Part("hasBackground") int hasBackground,
                                                @Part("backgroundImage") RequestBody backgroundImage);

    @POST("getKeyboardLayoutById")
    @FormUrlEncoded
    Call<GetKeyboardLayoutResponse> getKeyboardLayout(@Field("keyboardId") long keyboardId);

    @POST("getKeyboardById")
    @FormUrlEncoded
    Call<GetKeyboardByIdResponse> getKeyboardById(@Field("keyboardId") String keyboardId);

    @POST("getAllDevices")
    Call<GetAllDevicesResponse> getAllDevices();

    @POST("getKeysForDevice")
    @FormUrlEncoded
    Call<GetActionsForDeviceResponse> getKeysForDevice(@Field("deviceId") long deviceId);

    @POST("updateKeyboardLayoutById")
    @FormUrlEncoded
    Call<BaseResponse> updateKeysLayout(@Field("keyboardId") long keyboardId, @Field("keysLayout") String keysLayout);
}
