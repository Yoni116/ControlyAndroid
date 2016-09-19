package net.controly.controly.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.response.AddNewLocationResponse;
import net.controly.controly.http.service.EventService;
import net.controly.controly.model.User;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLocationActivity extends BaseLocationMapActivity {
    private Marker mLastMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePlacesAutoComplete();
    }

    @Override
    protected boolean placeAutoCompleteEnabled() {
        return true;
    }

    /**
     * In this case we want to fetch the locations lazily, because we have already fetched the locations in the previous activity.
     *
     * @return false.
     */
    @Override
    protected boolean fetchLocationsEagerly() {
        return false;
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

                //Shows a snackbar asking to save the new location.
                showSnackbar();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        //Create location only if it wasn't created before
                        if (!marker.equals(mLastMarker)) {
                            return false;
                        }

                        showAlertDialog();
                        return false;
                    }
                });

                //Zoom camera to new place.
                zoomCamera(place.getLatLng(), 20);
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
     * Show a snackbar suggesting to save new the new location.
     */
    private void showSnackbar() {
        final String snackbarTitle = "Would you like to save the new location?";
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, snackbarTitle, Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.GREEN).setAction("SAVE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        View snackbarView = snackbar.getView();
        TextView snackbarText = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackbarText.setTextColor(Color.WHITE);

        snackbar.show();
    }

    /**
     * If user wants to save the location, ask for the description of the new location.
     */
    private void showAlertDialog() {
        final EditText input = new EditText(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(layoutParams);

        new AlertDialog.Builder(mContext, R.style.ControlyDialog_Light_Dialog)
                .setTitle("Enter the new location's description:")
                .setView(input)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNewLocation(mLastMarker.getPosition(), input.getText().toString());
                    }
                })
                .create()
                .show();
    }

    /**
     * Save the new location.
     *
     * @param position    The position of the new location.
     * @param description The description of the new location.
     */
    private void saveNewLocation(LatLng position, String description) {
        ControlyApplication instance = ControlyApplication.getInstance();

        User authenticated = instance.getAuthenticatedUser();
        Call<AddNewLocationResponse> call = instance.getService(EventService.class)
                .createNewLocation(authenticated.getId(), position.latitude, position.longitude, description);

        call.enqueue(new Callback<AddNewLocationResponse>() {
            @Override
            public void onResponse(Call<AddNewLocationResponse> call, Response<AddNewLocationResponse> response) {
                Logger.info("New location creation has succeeded.");
                Toast.makeText(mContext, "Added new location", Toast.LENGTH_SHORT)
                        .show();

            }

            @Override
            public void onFailure(Call<AddNewLocationResponse> call, Throwable t) {
                Logger.error("New Location creation has failed.\n" + t.getMessage());
            }
        });
    }
}
