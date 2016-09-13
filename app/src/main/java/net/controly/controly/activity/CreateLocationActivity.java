package net.controly.controly.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.service.EventService;
import net.controly.controly.model.Location;
import net.controly.controly.model.User;
import net.controly.controly.util.LocationsUtils;
import net.controly.controly.util.Logger;
import net.controly.controly.util.PermissionUtils;
import net.controly.controly.util.UIUtils;

import retrofit2.Call;

public class CreateLocationActivity extends BaseActivity implements OnMapReadyCallback {
    public static final String LOCATIONS_LIST_EXTRA = "LOCATIONS_LIST";
    private Context mContext;

    private GoogleMap mMap;
    private Location[] mLocations;
    private Marker mLastMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mLocations = (Location[]) extras.get(LOCATIONS_LIST_EXTRA);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initializePlacesAutoComplete();
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

        for (Location location : mLocations) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(location.getTitle());

            mMap.addMarker(markerOptions);
        }

        //Set compass button in map.
        mMap.setPadding(0, 300, 0, 0);
        mMap.getUiSettings().setCompassEnabled(true);

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

    private void initializePlacesAutoComplete() {
        //Set a listener for selecting a place from the auto complete.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Log the place that was selected.
                Logger.info("Place '" + place.getName() + "' was selected.");

                //Add a marker of the selected place.
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName().toString());

                mLastMarker = mMap.addMarker(markerOptions);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        final EditText input = new EditText(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        input.setLayoutParams(layoutParams);

                        AlertDialog alertBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme)
                                .setTitle("Would you like to create this location?")
                                .setView(input)
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        createNewLocation(marker.getPosition(), input.getText().toString());
                                    }
                                })
                                .create();
                        alertBuilder.show();
                        return false;
                    }
                });

                //Zoom camera to new place.
                zoomCamera(place.getLatLng(), 20, 1500);
            }

            @Override
            public void onError(Status status) {
            }
        });

        //When clearing the text from the auto complete view, remove the added marker.
        View parentView = autocompleteFragment.getView();
        if (parentView != null) {
            final View clearButton = parentView.findViewById(R.id.place_autocomplete_clear_button);
            final View.OnClickListener defaultClick = UIUtils.getOnClickListener(clearButton);

            if (clearButton != null) {
                clearButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        defaultClick.onClick(clearButton);

                        mLastMarker.remove();
                        mLastMarker = null;
                    }
                });
            }
        }
    }

    /**
     * Save the new location.
     *
     * @param position The position of the new location.
     * @param title    The title of the new location.
     */
    private void createNewLocation(LatLng position, String title) {
        ControlyApplication instance = ControlyApplication.getInstance();

        User authenticated = instance.getAuthenticatedUser();
        Call call = instance.getService(EventService.class)
                .createNewLocation(authenticated.getId(), position.latitude, position.longitude, title);
    }

    /**
     * Focus the camera on the current device location.
     */
    private void focusCameraOnCurrentLocation() {
        zoomCamera(LocationsUtils.getCurrentLocation(mContext), 10, 1500);
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
    }
}
