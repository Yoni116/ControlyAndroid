package net.controly.controly.http.service;

import net.controly.controly.http.response.AddNewLocationResponse;
import net.controly.controly.http.response.DeleteEventResponse;
import net.controly.controly.http.response.GetEventsResponse;
import net.controly.controly.http.response.GetLocationsResponse;
import net.controly.controly.http.response.SearchAutomationsResponse;

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
    Call<AddNewLocationResponse> createNewLocation(@Field("userId") long userId, @Field("Latitude") double latitude,
                                                   @Field("Longitude") double longitude, @Field("Description") String description);

    @POST("deleteEvent")
    @FormUrlEncoded
    Call<DeleteEventResponse> deleteEvent(@Field("userId") long userId, @Field("eventId") long eventId);

    @POST("searchAutomationByName")
    @FormUrlEncoded
    Call<SearchAutomationsResponse> searchAutomations(@Field("userId") long userId, @Field("name") String query,
                                                      @Field("offset") int offset, @Field("automation") int automation);
}