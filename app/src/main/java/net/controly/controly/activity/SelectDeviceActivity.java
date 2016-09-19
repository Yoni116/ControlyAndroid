package net.controly.controly.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.response.GetAllDevicesResponse;
import net.controly.controly.http.service.KeyboardService;
import net.controly.controly.model.Device;
import net.controly.controly.model.EventBuilder;
import net.controly.controly.model.Key;
import net.controly.controly.util.EventCreationUtils;
import net.controly.controly.util.Logger;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is used to select a device to create a key for in the keyboard activity.
 */
public class SelectDeviceActivity extends BaseActivity {

    public static final String UPDATE_KEYS_LIST_EXTRA = "REMOVE_INDEX";
    public static final String ADD_KEY_EXTRA = "KEY_DATA";

    private Context mContext;
    private SelectActionActivity.Purpose mPurpose;
    private BoxListAdapter<Device> mDevicesListAdapter;

    private ArrayList<Key> mActions;
    private FloatingActionButton mActionsFab;

    private EventBuilder mEventBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        mContext = this;

        configureToolbar(true, false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPurpose = (SelectActionActivity.Purpose) extras.getSerializable(EventCreationUtils.PURPOSE_EXTRA);

            //If we are creating a new event, get the event builder object
            if (mPurpose == SelectActionActivity.Purpose.EVENT_CREATION) {
                mEventBuilder = (EventBuilder) extras.getSerializable(EventCreationUtils.EVENT_BUILDER_OBJECT_EXTRA);
            }
        }

        mDevicesListAdapter = new BoxListAdapter<>(this);
        ListView devicesList = (ListView) findViewById(R.id.devices_list);
        devicesList.setAdapter(mDevicesListAdapter);

        //Set a click listener for selecting a device.
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                //When clicking a device, move to the select action activity.
                Intent intent = new Intent(mContext, SelectActionActivity.class);

                //Pass the chosen device and the purpose of the action to the select action activity.
                intent.putExtra(SelectActionActivity.DEVICE_OBJECT_EXTRA, mDevicesListAdapter.getItem(i));
                intent.putExtra(EventCreationUtils.PURPOSE_EXTRA, mPurpose);
                intent.putExtra(EventCreationUtils.EVENT_BUILDER_OBJECT_EXTRA, mEventBuilder);

                startActivityForResult(intent, 1);

                //Show a slide animation.
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });

        //The actions that the user wishes to control with the key.
        mActions = new ArrayList<>(10);
        mActionsFab = (FloatingActionButton) findViewById(R.id.chosen_actions_fab);
        mActionsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show my actions
                Intent intent = new Intent(mContext, MyActionsActivity.class);
                intent.putExtra(MyActionsActivity.MY_ACTIONS_EXTRA, mActions);

                startActivityForResult(intent, 2);
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

            case R.id.create_key_menu_choose_automation:
                Intent intent = new Intent(mContext, MyAutomationsActivity.class);
                startActivity(intent);

            case R.id.create_key_menu_save_key:
                createKey();
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                //If the bundle has a request to add a new key, add it.
                if (extras.containsKey(ADD_KEY_EXTRA)) {
                    mActions.add((Key) extras.getSerializable(ADD_KEY_EXTRA));
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras.containsKey(UPDATE_KEYS_LIST_EXTRA)) {
                    mActions = (ArrayList<Key>) data.getSerializableExtra(UPDATE_KEYS_LIST_EXTRA);
                }
            }
        }

        //Show or hide the fab depending on whether it is empty or not.
        mActionsFab.setVisibility(mActions.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void loadDevices() {
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
                Logger.error("Problem while trying to load devices.\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void createKey() {
        if (mActions.isEmpty()) {
            Snackbar.make(findViewById(R.id.select_device_layout), "Please choose at least one action", Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            input.setLayoutParams(layoutParams);

            if (mActions.size() > 1) {
                new AlertDialog.Builder(this, R.style.ControlyDialog_Light_Dialog)
                        .setTitle(R.string.new_automation_dialog_text)
                        .setView(input)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        }
    }
}