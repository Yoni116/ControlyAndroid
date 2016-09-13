package net.controly.controly.http.service;

import net.controly.controly.http.response.GetEventsResponse;
import net.controly.controly.http.response.GetLocationsResponse;

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
    Call<GetEventsResponse> searchEvents(@Field("userId") long userId, @Field("keyword") String keyword,
                                         @Field("offset") int offset, @Field("sort") int sort);

    @POST("searchLocationsWithId")
    @FormUrlEncoded
    Call<GetLocationsResponse> searchLocations(@Field("userId") long userId, @Field("keyword") String keyword,
                                               @Field("offset") int offset, @Field("sort") int sort);


    @POST("getAllTriggers")
    Call<GetAllTriggersResponse> getAllTriggers();

    @POST("addNewLocation")
    @FormUrlEncoded
    Call createNewLocation(@Field("userId") long userId, @Field("Latitude") double latitude,
                           @Field("Longitude") double longitude, @Field("Description") String description);
}