package net.controly.controly.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.KeyboardListAdapter;
import net.controly.controly.http.response.GetAllUserKeyboardsResponse;
import net.controly.controly.http.service.UserService;
import net.controly.controly.model.Keyboard;
import net.controly.controly.model.User;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private Context mContext;

    private FloatingActionButton mMenuButton;
    private SwipeMenuListView mKeyboardList;
    private KeyboardListAdapter mKeyboardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //Set the main menu floating action button.
        mMenuButton = (FloatingActionButton) findViewById(R.id.open_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });

        //Set the keyboard list and it's adapter.
        mKeyboardList = (SwipeMenuListView) findViewById(R.id.keyboard_list);
        mKeyboardListAdapter = new KeyboardListAdapter(this);

        mKeyboardList.setAdapter(mKeyboardListAdapter);

        //Set on click listener for keyboard list.
        mKeyboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Clicked " + mKeyboardListAdapter.getItem(position).getName(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

        //Set the swipe menu button for the list.
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                //Set the 'more' button.
                SwipeMenuItem moreItem = new SwipeMenuItem(getApplicationContext());

                moreItem.setBackground(R.color.light_grey);
                moreItem.setWidth(400);
                moreItem.setTitle("More");
                moreItem.setTitleSize(15);
                moreItem.setTitleColor(Color.WHITE);

                menu.addMenuItem(moreItem);

                //Set the 'publish' button.
                SwipeMenuItem publish = new SwipeMenuItem(getApplicationContext());

                publish.setBackground(R.color.light_green);
                publish.setWidth(400);
                publish.setTitle("Publish");
                publish.setTitleSize(15);
                publish.setTitleColor(Color.WHITE);

                menu.addMenuItem(publish);
            }
        };

        mKeyboardList.setMenuCreator(creator);

        //Set the onclick listener for swipe menu buttons.
        mKeyboardList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Logger.info("User clicked on '" + menu.getMenuItem(index).getTitle() + "' button in the swipe menu.");

                switch (index) {
                    case 0:
                        final String[] options = {"Delete", "Edit"};
                        new AlertDialog.Builder(mContext)
                                .setTitle("Choose an option")
                                .setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                Toast.makeText(MainActivity.this, "Will delete...", Toast.LENGTH_SHORT)
                                                        .show();
                                                break;
                                            case 1:
                                                Toast.makeText(MainActivity.this, "Will edit...", Toast.LENGTH_SHORT)
                                                        .show();
                                                break;
                                        }
                                    }
                                }).show();
                    case 1:
                        Toast.makeText(MainActivity.this, "Will lunch the publish activity.", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }

                //Return true to keep swipe menu open when clicking one of the buttons
                return true;
            }
        });

        loadKeyboards();
    }

    /**
     * This method loads the keyboards from the API asynchronously.
     */
    public void loadKeyboards() {
        //Show a wait dialog while loading the keyboards.
        showWaitDialog();

        User authenticatedUser = ControlyApplication.getInstance()
                .getAuthenticatedUser();

        //Get the keyboard list for the authenticated user.
        Call<GetAllUserKeyboardsResponse> call = ControlyApplication.getInstance()
                .getService(UserService.class)
                .getAllUserKeyboards(authenticatedUser.getId());

        call.enqueue(new Callback<GetAllUserKeyboardsResponse>() {
            @Override
            public void onResponse(Call<GetAllUserKeyboardsResponse> call, Response<GetAllUserKeyboardsResponse> response) {
                dismissDialog();

                //Avoid NullPointerException
                if (response.body() == null || !response.body().hasSucceeded()) {
                    Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                            .show();
                    UIUtils.startActivity(getApplicationContext(), LoginActivity.class);
                    return;
                }

                //Log that the request was successful and add the keyboards to the list.
                List<Keyboard> keyboards = response.body().getKeyboards();
                Logger.info("Received user keyboards - there are " + keyboards.size() + " keyboards.");

                mKeyboardListAdapter.addAll(keyboards);
            }

            @Override
            public void onFailure(Call<GetAllUserKeyboardsResponse> call, Throwable t) {
                Logger.error("Problem while trying to receive keyboards\n" + t.getMessage());
                dismissDialog();
            }
        });
    }

}
