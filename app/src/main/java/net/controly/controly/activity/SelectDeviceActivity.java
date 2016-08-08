package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.DeviceListAdapter;
import net.controly.controly.http.response.GetAllDevicesResponse;
import net.controly.controly.http.service.KeyboardService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Itai on 08-Aug-16.
 */
public class SelectDeviceActivity extends BaseActivity {

    private Context mContext;

    private Toolbar mToolbar;

    private DeviceListAdapter mDevicesListAdapter;
    private ListView mDevicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        this.mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("Select a device");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDevicesList = (ListView) findViewById(R.id.devices_list);
        mDevicesListAdapter = new DeviceListAdapter(this);
        mDevicesList.setAdapter(mDevicesListAdapter);

        mDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }

                Intent intent = new Intent(mContext, SelectActionActivity.class);
                intent.putExtra(SelectActionActivity.DEVICE_ID_EXTRA, mDevicesListAdapter.getItem(i).getId());
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });

        Call<GetAllDevicesResponse> call = ControlyApplication.getInstance()
                .getService(KeyboardService.class)
                .getAllDevices();

        call.enqueue(new Callback<GetAllDevicesResponse>() {
            @Override
            public void onResponse(Call<GetAllDevicesResponse> call, Response<GetAllDevicesResponse> response) {
                mDevicesListAdapter.addAll(response.body().getDevices());
            }

            @Override
            public void onFailure(Call<GetAllDevicesResponse> call, Throwable t) {

            }
        });
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
