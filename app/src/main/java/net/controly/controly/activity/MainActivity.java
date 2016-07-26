package net.controly.controly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.KeyboardListAdapter;
import net.controly.controly.http.response.GetAllUserKeyboardsResponse;
import net.controly.controly.http.service.UserService;
import net.controly.controly.model.Keyboard;
import net.controly.controly.model.User;
import net.controly.controly.util.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private FloatingActionButton mMenuButton;
    private ListView mKeyboardList;
    private KeyboardListAdapter mKeyboardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuButton = (FloatingActionButton) findViewById(R.id.open_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });

        mKeyboardList = (ListView) findViewById(R.id.keyboard_list);
        mKeyboardListAdapter = new KeyboardListAdapter(this);

        mKeyboardList.setAdapter(mKeyboardListAdapter);

        mKeyboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Clicked " + mKeyboardListAdapter.getItem(position).getName(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

        loadKeyboards();
    }

    public void loadKeyboards() {
        showWaitDialog();

        User authenticatedUser = ControlyApplication.getInstace()
                .getAuthenticatedUser();

        Call<GetAllUserKeyboardsResponse> call = ControlyApplication.getInstace()
                .getService(UserService.class)
                .getAllUserKeyboards(authenticatedUser.getId());

        call.enqueue(new Callback<GetAllUserKeyboardsResponse>() {
            @Override
            public void onResponse(Call<GetAllUserKeyboardsResponse> call, Response<GetAllUserKeyboardsResponse> response) {
                //Log that the request was successful and add the keyboards to the list.

                List<Keyboard> keyboards = response.body().getKeyboards();
                Logger.info("Received user keyboards. There are " + keyboards.size() + " keyboards.");

                mKeyboardListAdapter.addAll(keyboards);
                dismissDialog();
            }

            @Override
            public void onFailure(Call<GetAllUserKeyboardsResponse> call, Throwable t) {
                Logger.error("Problem while trying to receive keyboards\n" + t.getMessage());
                dismissDialog();
            }
        });
    }

}
