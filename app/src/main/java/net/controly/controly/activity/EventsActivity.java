package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.response.GetEventsResponse;
import net.controly.controly.http.service.EventService;
import net.controly.controly.model.Event;
import net.controly.controly.model.User;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is an activity for searching events.
 */
public class EventsActivity extends BaseActivity {

    private Context mContext;
    private BoxListAdapter<Event> mEventListAdapter;

    private SwipeMenuListView mEventListview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mContext = this;

        configureToolbar(true, false);

        mEventListAdapter = new BoxListAdapter<>(mContext);
        mEventListview = (SwipeMenuListView) findViewById(R.id.events_list);
        mEventListview.setAdapter(mEventListAdapter);

        setupSwipeMenu();

        loadEvents("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_activity_menu, menu);

        //Set search bar hint text
        MenuItem searchMenuItem = menu.findItem(R.id.search_bar);

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.events_activity_search_bar_hint));
        UIUtils.setSearchBarTextColor(this, searchView);

        //Listener for searching for events.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadEvents(newText);
                return true;
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
                loadEvents("");
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //In case that the user wants to create a new event.
            case R.id.main_menu_create_event_button:
                Intent intent = new Intent(mContext, SelectTriggerActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSwipeMenu() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteEvent = new SwipeMenuItem(mContext);
                deleteEvent.setBackground(android.R.color.holo_red_light);
                deleteEvent.setTitleColor(Color.WHITE);
                deleteEvent.setTitle("Delete");
                deleteEvent.setTitleSize(15);
                deleteEvent.setWidth(300);

                menu.addMenuItem(deleteEvent);
            }
        };

        mEventListview.setMenuCreator(swipeMenuCreator);
    }

    /**
     * Query the events with the given text.
     *
     * @param query The query for the events.
     */
    private void loadEvents(String query) {
        Logger.info("Searching for events with the text: '" + query + "'.");

        //Clear previous search
        mEventListAdapter.clear();

        //Query the events
        ControlyApplication application = ControlyApplication.getInstance();
        User authenticated = application.getAuthenticatedUser();

        Call<GetEventsResponse> call = application.getService(EventService.class)
                .searchEvents(authenticated.getId(), query, 0, 2);
        call.enqueue(new Callback<GetEventsResponse>() {
            @Override
            public void onResponse(Call<GetEventsResponse> call, Response<GetEventsResponse> response) {
                //Add the events to the ListView
                Event[] events = response.body().getEvents();
                mEventListAdapter.addAll(events);

                Logger.info("Found " + events.length + " events.");
            }

            @Override
            public void onFailure(Call<GetEventsResponse> call, Throwable t) {
                Logger.error("Problem while trying to search events.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
