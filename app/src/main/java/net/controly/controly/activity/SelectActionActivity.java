package net.controly.controly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.response.GetActionsForDeviceResponse;
import net.controly.controly.http.response.SearchAutomationsResponse;
import net.controly.controly.http.service.EventService;
import net.controly.controly.http.service.KeyboardService;
import net.controly.controly.model.Device;
import net.controly.controly.model.EventBuilder;
import net.controly.controly.model.Key;
import net.controly.controly.model.User;
import net.controly.controly.util.EventCreationUtils;
import net.controly.controly.util.Logger;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is for choosing an action to create a key for the keyboard.
 */
public class SelectActionActivity extends BaseActivity {

    public static final String DEVICE_OBJECT_EXTRA = "DEVICE_OBJECT";

    private Device mDevice;
    private Purpose mPurpose;
    private EventBuilder mEventBuilder;

    private BoxListAdapter<Key> mActionsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Logger.error("No device id for receiving actions");
            Toast.makeText(this, "Controly encountered an error", Toast.LENGTH_SHORT) //Phrase a better message
                    .show();

            finish();
            return;
        }

        mDevice = (Device) extras.getSerializable(DEVICE_OBJECT_EXTRA);
        mPurpose = (Purpose) extras.getSerializable(EventCreationUtils.PURPOSE_EXTRA);

        //If we are creating a new event, get the event details.
        if (mPurpose == Purpose.EVENT_CREATION) {
            mEventBuilder = (EventBuilder) extras.getSerializable(EventCreationUtils.EVENT_BUILDER_OBJECT_EXTRA);
        }

        setTitle(mDevice.getName());
        configureToolbar(true, false);

        mActionsListAdapter = new BoxListAdapter<>(this);
        ListView actionsListView = (ListView) findViewById(R.id.actions_list);
        actionsListView.setAdapter(mActionsListAdapter);

        actionsListView.setOnItemClickListener(OnActionItemClick());

        loadUserActions();
        loadDeviceActions();
    }

    private AdapterView.OnItemClickListener OnActionItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int index, long l) {
                Intent intent = new Intent();
                intent.putExtra(SelectDeviceActivity.ADD_KEY_EXTRA, mActionsListAdapter.getItem(index));
                setResult(RESULT_OK, intent);

                onBackPressed();
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get the actions for the selected device.
     */
    private void loadDeviceActions() {
        Call<GetActionsForDeviceResponse> call = ControlyApplication.getInstance()
                .getService(KeyboardService.class)
                .getKeysForDevice(mDevice.getId());

        call.enqueue(new Callback<GetActionsForDeviceResponse>() {
            @Override
            public void onResponse(Call<GetActionsForDeviceResponse> call, Response<GetActionsForDeviceResponse> response) {
                mActionsListAdapter.addAll(response.body().getDeviceKeys());
            }

            @Override
            public void onFailure(Call<GetActionsForDeviceResponse> call, Throwable t) {
                Logger.error("Problem while trying to load device actions.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void loadUserActions() {

        //Load user actions only if this is for the pc.
        if (mDevice.getId() != 1) {
            return;
        }

        ControlyApplication instance = ControlyApplication.getInstance();
        User authenticated = instance.getAuthenticatedUser();

        Call<SearchAutomationsResponse> call = instance.getService(EventService.class)
                .searchAutomations(authenticated.getId(), "", 0, 0);
        call.enqueue(new Callback<SearchAutomationsResponse>() {
            @Override
            public void onResponse(Call<SearchAutomationsResponse> call, Response<SearchAutomationsResponse> response) {
                mActionsListAdapter.addAll(response.body().getAutomations());
            }

            @Override
            public void onFailure(Call<SearchAutomationsResponse> call, Throwable t) {
                Logger.error("Problem while trying to load user actions.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * This enum represents the purpose of selecting a device action.
     */
    public enum Purpose implements Serializable {
        KEY_CREATION, EVENT_CREATION
    }
}
