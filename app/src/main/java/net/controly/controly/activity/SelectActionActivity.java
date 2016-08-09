package net.controly.controly.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.ActionListAdapter;
import net.controly.controly.http.response.GetKeysForDeviceResponse;
import net.controly.controly.http.service.KeyboardService;
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

    private Toolbar mToolbar;

    private ActionListAdapter mActionsListAdapter;
    private ListView mActionsListView;

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

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("Select an action");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mActionsListAdapter = new ActionListAdapter(this);
        mActionsListView = (ListView) findViewById(R.id.actions_list);
        mActionsListView.setAdapter(mActionsListAdapter);

        Call<GetKeysForDeviceResponse> call = ControlyApplication.getInstance()
                .getService(KeyboardService.class)
                .getKeysForDevice(deviceId);

        call.enqueue(new Callback<GetKeysForDeviceResponse>() {
            @Override
            public void onResponse(Call<GetKeysForDeviceResponse> call, Response<GetKeysForDeviceResponse> response) {
                mActionsListAdapter.addAll(response.body().getActions());
            }

            @Override
            public void onFailure(Call<GetKeysForDeviceResponse> call, Throwable t) {

            }
        });
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

        return true;
    }
}
