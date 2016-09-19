package net.controly.controly.util;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class contains an assortment of util methods related to location.
 */
public class LocationsUtils {

    /**
     * @return The user's current location.
     */
    @SuppressWarnings("MissingPermission")
    public static LatLng getCurrentLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (PermissionUtils.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION) &&
                PermissionUtils.hasPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            //TODO This might crash if location services are turned off.
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            return new LatLng(latitude, longitude);
        }

        return null;
    }
}
