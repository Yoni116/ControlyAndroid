package net.controly.controly.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.controly.controly.R;
import net.controly.controly.model.Location;
import net.controly.controly.util.LocationsUtils;
import net.controly.controly.util.Logger;
import net.controly.controly.util.PermissionUtils;
import net.controly.controly.util.UIUtils;

public class CreateLocationActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String LOCATIONS_LIST_EXTRA = "LOCATIONS_LIST";
    private Context mContext;

    private GoogleMap mMap;
    private Location[] mLocations;
    private boolean focusingOnCurrentLocation = false;

    private FloatingActionButton myLocationFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mLocations = (Location[]) extras.get(LOCATIONS_LIST_EXTRA);
        }

        //Request both of the permissions to access the user's location.
        PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "");
        PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, "");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Logger.info("Place '" + place.getName() + "' was selected.");

                focusingOnCurrentLocation = false;

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName().toString());

                mMap.addMarker(markerOptions);
                zoomCamera(place.getLatLng(), 20, 2000);
            }

            @Override
            public void onError(Status status) {
            }
        });

        myLocationFab = (FloatingActionButton) findViewById(R.id.my_location_fab);
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusCameraOnCurrentLocation();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (Location location : mLocations) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(location.getTitle());

            mMap.addMarker(markerOptions);
        }

        //Focus camera on current location if user gave permission.
        if (PermissionUtils.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            focusCameraOnCurrentLocation();
        }

        //Set icon color back to black when focusing on current location, only if API is updated.
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (!focusingOnCurrentLocation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    UIUtils.changeFabIconColor(myLocationFab, Color.parseColor("#FF000000"));
                }
            }
        });
    }

    /**
     * Wait for the permission to access device's location to be granted. Then, move the map's camera to the location.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.GRANT_PERMISSION_REQUEST_CODE:
                if (PermissionUtils.permissionGranted(grantResults)) {
                    focusCameraOnCurrentLocation();
                }
        }
    }

    /**
     * Focus the camera on the current device location.
     */
    private void focusCameraOnCurrentLocation() {
        focusingOnCurrentLocation = true;
        zoomCamera(LocationsUtils.getCurrentLocation(mContext), 10, 2000);

        //Set icon color to blue when focusing on current location, only if API is updated.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UIUtils.changeFabIconColor(myLocationFab, getResources().getColor(R.color.color_accent));
        }
    }

    /**
     * Zooms the camera on the given location.
     *
     * @param location The location to zoom at.
     * @param zoom     The amount of zoom.
     * @param duration The duration of the animation.
     */
    private void zoomCamera(LatLng location, int zoom, int duration) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, zoom);
        mMap.animateCamera(cameraUpdate, duration, null);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (focusingOnCurrentLocation) {
                    focusingOnCurrentLocation = false;
                }
            }
        });
    }
}
