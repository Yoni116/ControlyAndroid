package net.controly.controly.http.response;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.model.Location;

/**
 * This class represents a response from the API for searching for locations by name.
 */
public class GetLocationsResponse extends BaseResponse {

    /**
     * The locations received from the request.
     */
    @SerializedName("data")
    private Location[] locations;

    public GetLocationsResponse(Location[] locations) {
        this.locations = locations;
    }

    public Location[] getLocations() {
        return locations;
    }
}
