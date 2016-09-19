package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.service.EventService;
import net.controly.controly.http.service.GetAllTriggersResponse;
import net.controly.controly.model.EventBuilder;
import net.controly.controly.model.Trigger;
import net.controly.controly.model.User;
import net.controly.controly.util.DateUtils;
import net.controly.controly.util.EventCreationUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is an activity for selecting a trigger of an mEvent.
 */
public class SelectTriggerActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    private Context mContext;
    private BoxListAdapter<Trigger> mTriggersListAdapter;

    private EventBuilder mEventBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trigger);

        configureToolbar(true, false);

        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEventBuilder = (EventBuilder) extras.getSerializable(EventCreationUtils.EVENT_BUILDER_OBJECT_EXTRA);
        }

        //Initialize the list view
        mTriggersListAdapter = new BoxListAdapter<>(this);

        ListView triggersListView = (ListView) findViewById(R.id.trigger_listview);
        triggersListView.setAdapter(mTriggersListAdapter);
        triggersListView.setOnItemClickListener(getTriggerItemClickListener());

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

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = DateUtils.FormatTime(hourOfDay, minute, second);

        JsonObject triggerInfo = new JsonObject();
        triggerInfo.addProperty("time", time);

        mEventBuilder.triggerInfo(triggerInfo.toString());

        Intent intent = new Intent(this, SelectDeviceActivity.class);

        intent.putExtra(EventCreationUtils.PURPOSE_EXTRA, SelectActionActivity.Purpose.EVENT_CREATION);
        intent.putExtra(EventCreationUtils.EVENT_BUILDER_OBJECT_EXTRA, mEventBuilder);

        startActivity(intent);
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
                //TODO Show error
            }
        });
    }

    private AdapterView.OnItemClickListener getTriggerItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Trigger clicked = mTriggersListAdapter.getItem(position);

                User authenticated = ControlyApplication.getInstance()
                        .getAuthenticatedUser();

                mEventBuilder = EventBuilder.getInstance()
                        .triggerId(clicked.getTriggerId())
                        .userId(authenticated.getId());

                //Create the event based on its type.
                switch (clicked.getTriggerType()) {
                    //In case that the user wants to create a location based event.
                    case LOCATION:
                        Intent intent = new Intent(mContext, SelectLocationActivity.class);
                        intent.putExtra(EventCreationUtils.EVENT_BUILDER_OBJECT_EXTRA, mEventBuilder);
                        startActivity(intent);
                        break;

                    //In case that the user wants to create a time based location.
                    case TIME:
                        Calendar now = Calendar.getInstance();
                        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(SelectTriggerActivity.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                now.get(Calendar.SECOND),
                                false);

                        timePickerDialog.setTitle(clicked.getTitle());
                        timePickerDialog.show(getFragmentManager(), "timepicker");
                        break;
                }
            }
        };
    }
}
