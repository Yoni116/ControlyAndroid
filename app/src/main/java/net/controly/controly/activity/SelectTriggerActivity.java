package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private Context mContext;
    private BoxListAdapter<Trigger> mTriggersListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trigger);

        configureToolbar("Event trigger", true, false);

        mContext = this;

        //Initialize the list view
        mTriggersListAdapter = new BoxListAdapter<>(this);
        ListView triggersListView = (ListView) findViewById(R.id.trigger_listview);
        triggersListView.setAdapter(mTriggersListAdapter);

        triggersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, SelectLocationActivity.class);
                intent.putExtra(SelectLocationActivity.TRIGGER_OBJECT_EXTRA, mTriggersListAdapter.getItem(position));

                startActivity(intent);
            }
        });

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
