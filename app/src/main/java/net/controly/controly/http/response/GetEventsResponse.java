package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Event;

/**
 * This class represents a response from the API for searching for events by name.
 */
public class GetEventsResponse extends BaseResponse {

    /**
     * The events received from the request.
     */
    @SerializedName("data")
    private Event[] events;

    public GetEventsResponse(Event[] events) {
        this.events = events;
    }

    public Event[] getEvents() {
        return events;
    }
}
