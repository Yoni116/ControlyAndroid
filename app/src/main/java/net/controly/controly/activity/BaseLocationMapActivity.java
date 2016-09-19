package net.controly.controly.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.response.GetLocationsResponse;
import net.controly.controly.http.service.EventService;
import net.controly.controly.model.Location;
import net.controly.controly.model.User;
import net.controly.controly.util.LocationsUtils;
import net.controly.controly.util.Logger;
import net.controly.controly.util.PermissionUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is a base activity for showing the user's places.
 */
public abstract class BaseLocationMapActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String LOCATIONS_LIST_EXTRA = "LOCATIONS_LIST";

    protected Context mContext;
    protected CoordinatorLayout mCoordinatorLayout;

    protected GoogleMap mMap;
    protected PlaceAutocompleteFragment mPlaceAutoComplete;
    protected Location[] mLocations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        mContext = this;
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        //If the user asked to eagerly fetch the locations, fetch them from the server.
        if (fetchLocationsEagerly()) {
            ControlyApplication instance = ControlyApplication.getInstance();
            User authenticatedUser = instance.getAuthenticatedUser();

            Call<GetLocationsResponse> call = instance.getService(EventService.class)
                    .searchLocations(authenticatedUser.getId(), "", 0, 2);
            call.enqueue(new Callback<GetLocationsResponse>() {
                @Override
                public void onResponse(Call<GetLocationsResponse> call, Response<GetLocationsResponse> response) {
                    Logger.info("Locations fetched successfully.");
                    mLocations = response.body().getLocations();
                    addUserLocationMarkers();
                }

                @Override
                public void onFailure(Call<GetLocationsResponse> call, Throwable t) {
                    Logger.info("There was a problem trying to fetch the locations.");
                    Toast.makeText(BaseLocationMapActivity.this, "There was a connection error.", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } else { //If the user asked to lazily fetch the locations, fetch them from the intent extras.
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mLocations = (Location[]) extras.get(LOCATIONS_LIST_EXTRA);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //If we are fetching the locations eagerly, we should add the markers only on the response stage.
        //On the other hand, if we are fetching them lazily, we already have them at this stage.
        if (!fetchLocationsEagerly()) {
            addUserLocationMarkers();
        }

        //Show places autocomplete if enabled.
        mPlaceAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        showPlaceAutoComplete();

        //Set compass button in map.
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //If user has location permissions, zoom on his location and show button.
        //If not, request both of the permissions to access the user's location.
        if (PermissionUtils.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            focusCameraOnCurrentLocation();

            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "");
            PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, "");
        }
    }

    /**
     * Wait for the permission to access device's location to be granted. Then, move the map's camera to the location.
     */
    @Override
    @SuppressWarnings("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.GRANT_PERMISSION_REQUEST_CODE:
                if (PermissionUtils.permissionGranted(grantResults)) {
                    focusCameraOnCurrentLocation();

                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setMyLocationEnabled(true);
                }
        }
    }

    /**
     * @return This method decides whether to show or hide the places auto complete search bar.
     */
    protected abstract boolean placeAutoCompleteEnabled();

    /**
     * @return If we pass true to this function, it will fetch the locations from the server.
     * If we pass false, it will fetch the locations from the intent extras.
     */
    protected abstract boolean fetchLocationsEagerly();

    /**
     * Focus the camera on the current device location.
     */
    protected void focusCameraOnCurrentLocation() {
        zoomCamera(LocationsUtils.getCurrentLocation(mContext), 10);
    }

    /**
     * Zooms the camera on the given location.
     *  @param location The location to zoom at.
     * @param zoom     The amount of zoom.
     */
    protected void zoomCamera(LatLng location, int zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, zoom);
        mMap.animateCamera(cameraUpdate, 1500, null);
    }

    /**
     * Show place auto complete if user has enabled it.
     */
    private void showPlaceAutoComplete() {
        if (placeAutoCompleteEnabled()) {
            mPlaceAutoComplete.getFragmentManager()
                    .beginTransaction()
                    .show(mPlaceAutoComplete)
                    .commit();

            //Set padding so autocomplete search bar and buttons won't overlap each other.
            mMap.setPadding(0, 300, 0, 0);
        } else {
            mPlaceAutoComplete.getFragmentManager()
                    .beginTransaction()
                    .hide(mPlaceAutoComplete)
                    .commit();
        }
    }

    /**
     * Add markers of the user's locations to the map.
     */
    private void addUserLocationMarkers() {
        if (mLocations == null) {
            Logger.info("User has no locations.");
        } else {
            for (Location location : mLocations) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(location.getTitle());

                mMap.addMarker(markerOptions);
            }
        }
    }
}
