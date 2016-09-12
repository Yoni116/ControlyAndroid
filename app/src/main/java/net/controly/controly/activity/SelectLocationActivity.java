package net.controly.controly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.LocationsGridAdapter;
import net.controly.controly.http.response.GetLocationsResponse;
import net.controly.controly.http.service.EventService;
import net.controly.controly.model.Location;
import net.controly.controly.model.Trigger;
import net.controly.controly.model.User;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is an activity for selecting a location for the new event.
 */
public class SelectLocationActivity extends BaseActivity {

    public static final String TRIGGER_OBJECT_EXTRA = "CHOSEN_TRIGGER";
    private Trigger trigger;

    private LocationsGridAdapter mLocationListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        configureToolbar(getString(R.string.locations_activity_toolbar_title), true, false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trigger = (Trigger) extras.get(TRIGGER_OBJECT_EXTRA);
        }

        mLocationListAdapter = new LocationsGridAdapter(this);
        GridView locationsGridView = (GridView) findViewById(R.id.locations_grid);
        locationsGridView.setAdapter(mLocationListAdapter);

        loadLocations("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_location_menu, menu);

        //Set search bar hint text
        MenuItem searchMenuItem = menu.findItem(R.id.search_bar);

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.locations_activity_search_bar_hint));
        UIUtils.setSearchBarTextColor(this, searchView);

        //Listener for searching for events.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadLocations(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Show all events when collapsing the search bar.0
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                loadLocations("");
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_create_location_button:
                Intent intent = new Intent(this, CreateLocationActivity.class);
                intent.putExtra(CreateLocationActivity.LOCATIONS_LIST_EXTRA, mLocationListAdapter.getAll());
                startActivity(intent);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Query the events with the given text.
     *
     * @param query The query for the events.
     */
    private void loadLocations(String query) {
        Logger.info("Searching for location with the text: '" + query + "'.");

        //Clear previous search
        mLocationListAdapter.clear();

        //Query the events
        ControlyApplication application = ControlyApplication.getInstance();
        User authenticated = application.getAuthenticatedUser();

        Call<GetLocationsResponse> call = application.getService(EventService.class)
                .searchLocations(authenticated.getId(), query, 0, 2);
        call.enqueue(new Callback<GetLocationsResponse>() {
            @Override
            public void onResponse(Call<GetLocationsResponse> call, Response<GetLocationsResponse> response) {
                //Add the events to the ListView
                Location[] locations = response.body().getLocations();
                mLocationListAdapter.addAll(locations);

                Logger.info("Found " + locations.length + " events.");
            }

            @Override
            public void onFailure(Call<GetLocationsResponse> call, Throwable t) {
                Logger.error("Problem while trying to search for places.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
