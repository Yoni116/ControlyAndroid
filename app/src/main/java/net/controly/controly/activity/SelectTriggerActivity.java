package net.controly.controly.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.ListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.service.EventService;
import net.controly.controly.http.service.GetAllTriggersResponse;
import net.controly.controly.model.Trigger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is an activity for selecting a trigger of an mEvent.
 */
public class SelectTriggerActivity extends BaseActivity {

    private BoxListAdapter<Trigger> mTriggersListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trigger);

        configureToolbar("What triggers the event?");

        //Show back button on toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Initialize the list view
        mTriggersListAdapter = new BoxListAdapter<>(this);
        ListView triggersListView = (ListView) findViewById(R.id.trigger_listview);
        triggersListView.setAdapter(mTriggersListAdapter);

        loadTriggers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    private void loadTriggers() {
        Call<GetAllTriggersResponse> call = ControlyApplication.getInstance()
                .getService(EventService.class)
                .getAllTriggers();

        call.enqueue(new Callback<GetAllTriggersResponse>() {
            @Override
            public void onResponse(Call<GetAllTriggersResponse> call, Response<GetAllTriggersResponse> response) {
                mTriggersListAdapter.addAll(response.body().getTriggers());
            }

            @Override
            public void onFailure(Call<GetAllTriggersResponse> call, Throwable t) {
            }
        });
    }
}
