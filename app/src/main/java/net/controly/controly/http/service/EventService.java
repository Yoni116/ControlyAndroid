package net.controly.controly.http.service;

import net.controly.controly.http.response.GetEventsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * REST API for event operations.
 */
public interface EventService {

    @POST("searchEventsWithId")
    @FormUrlEncoded
    Call<GetEventsResponse> getEvent(@Field("userId") long userId, @Field("keyword") String keyword, @Field("offset") int offset);


    @POST("getAllTriggers")
    Call<GetAllTriggersResponse> getAllTriggers();
}