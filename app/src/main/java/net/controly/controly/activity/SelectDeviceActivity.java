package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.response.GetAllDevicesResponse;
import net.controly.controly.http.service.KeyboardService;
import net.controly.controly.model.Device;
import net.controly.controly.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is used to select a device to create a key for in the keyboard activity.
 */
public class SelectDeviceActivity extends BaseActivity {

    private Context mContext;

    private BoxListAdapter<Device> mDevicesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        mContext = this;

        configureToolbar("Select a device", true, false);

        mDevicesListAdapter = new BoxListAdapter<>(this, true);
        ListView devicesList = (ListView) findViewById(R.id.devices_list);
        devicesList.setAdapter(mDevicesListAdapter);

        //Set a click listener for selecting a device.
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }

                //When clicking a device, move to the select action activity.
                Intent intent = new Intent(mContext, SelectActionActivity.class);
                intent.putExtra(SelectActionActivity.DEVICE_ID_EXTRA, mDevicesListAdapter.getItem(i).getId());
                startActivity(intent);

                //Show a slide animation.
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });

        loadDevices();
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

    private void loadDevices() {
        Call<GetAllDevicesResponse> call = ControlyApplication.getInstance()
                .getService(KeyboardService.class)
                .getAllDevices();

        call.enqueue(new Callback<GetAllDevicesResponse>() {
            @Override
            public void onResponse(Call<GetAllDevicesResponse> call, Response<GetAllDevicesResponse> response) {
                mDevicesListAdapter.addFirstItem(0, new Device(0, "My Automations", null, null, null));
                mDevicesListAdapter.addAll(response.body().getDevices());
            }

            @Override
            public void onFailure(Call<GetAllDevicesResponse> call, Throwable t) {
                Logger.error("Problem while trying to load devices.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
