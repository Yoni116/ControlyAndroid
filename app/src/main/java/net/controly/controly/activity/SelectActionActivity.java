package net.controly.controly.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.response.GetActionsForDeviceResponse;
import net.controly.controly.http.service.KeyboardService;
import net.controly.controly.model.Action;
import net.controly.controly.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is for choosing an action to create a key for the keyboard.
 */
public class SelectActionActivity extends BaseActivity {

    public static final String DEVICE_ID_EXTRA = "DEVICE_ID";
    private long deviceId;

    private BoxListAdapter<Action> mActionsListAdapter;

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

        deviceId = extras.getLong(DEVICE_ID_EXTRA);

        configureToolbar("Select an action");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mActionsListAdapter = new BoxListAdapter<>(this);
        ListView actionsListView = (ListView) findViewById(R.id.actions_list);
        actionsListView.setAdapter(mActionsListAdapter);

        loadActions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_device_menu, menu);
        return true;
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
    private void loadActions() {
        Call<GetActionsForDeviceResponse> call = ControlyApplication.getInstance()
                .getService(KeyboardService.class)
                .getKeysForDevice(deviceId);

        call.enqueue(new Callback<GetActionsForDeviceResponse>() {
            @Override
            public void onResponse(Call<GetActionsForDeviceResponse> call, Response<GetActionsForDeviceResponse> response) {
                mActionsListAdapter.addAll(response.body().getActions());
            }

            @Override
            public void onFailure(Call<GetActionsForDeviceResponse> call, Throwable t) {
                Logger.error("Problem while trying to load device actions.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
