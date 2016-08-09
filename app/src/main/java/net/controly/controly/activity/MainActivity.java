package net.controly.controly.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.KeyboardListAdapter;
import net.controly.controly.http.response.DeleteKeyboardResponse;
import net.controly.controly.http.response.GetAllUserKeyboardsResponse;
import net.controly.controly.http.service.KeyboardService;
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

    //-------Views-------
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private TextView mKeyboardListEmptyText;

    private SwipeMenuListView mKeyboardList;
    private KeyboardListAdapter mKeyboardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //Set toolbar text and color
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(mContext, android.R.color.white));
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Controly");

        //Set the keyboard list and it's adapter.
        mKeyboardList = (SwipeMenuListView) findViewById(R.id.keyboard_list);
        mKeyboardListAdapter = new KeyboardListAdapter(this);

        mKeyboardList.setAdapter(mKeyboardListAdapter);

        mKeyboardListEmptyText = (TextView) findViewById(R.id.keyboard_list_empty_text);
        mKeyboardList.setEmptyView(mKeyboardListEmptyText);

        //Set on click listener for keyboard list.
        mKeyboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent controllerActivity = new Intent(mContext, KeyboardActivity.class);
                controllerActivity.putExtra(KeyboardActivity.CONTROLLER_OBJECT_EXTRA, mKeyboardListAdapter.getItem(position));

                startActivity(controllerActivity);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
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
            public boolean onMenuItemClick(int position, SwipeMenu menu, final int index) {
                Logger.info("User clicked on '" + menu.getMenuItem(index).getTitle() + "' button in the swipe menu.");

                switch (index) {
                    case 0:
                        final String[] options = {"Edit", "Delete"};
                        new AlertDialog.Builder(mContext)
                                .setTitle("Choose an option")
                                .setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                Toast.makeText(MainActivity.this, "Will edit...", Toast.LENGTH_SHORT)
                                                        .show();
                                                break;

                                            case 1:
                                                Keyboard keyboardToDelete = mKeyboardListAdapter.getItem(index);
                                                User user = ControlyApplication.getInstance()
                                                        .getAuthenticatedUser();

                                                Logger.info("Deleting keyboard with id #" + keyboardToDelete.getId());
                                                Call<DeleteKeyboardResponse> call = ControlyApplication.getInstance()
                                                        .getService(KeyboardService.class)
                                                        .deleteKeyboard(user.getId(), keyboardToDelete.getId());

                                                call.enqueue(new Callback<DeleteKeyboardResponse>() {
                                                    @Override
                                                    public void onResponse(Call<DeleteKeyboardResponse> call, Response<DeleteKeyboardResponse> response) {
                                                        if (response.body().hasSucceeded()) {
                                                            Logger.info("Keyboard delete succeeded");
                                                            loadKeyboards();
                                                        } else {
                                                            onFailure(call, null);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<DeleteKeyboardResponse> call, Throwable t) {
                                                        Logger.error("Keyboard delete failed");
                                                        Toast.makeText(mContext, "Could not delete the keyboard", Toast.LENGTH_SHORT)
                                                                .show();

                                                        if (t != null) {
                                                            Logger.error(t.getMessage());
                                                        }
                                                    }
                                                });

                                                break;
                                        }
                                    }
                                }).show();

                        break;
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

        if (mKeyboardList != null) {
            mKeyboardListAdapter.clear();
        }

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

                if (response.body().getKeyboards() == null) {
                    Logger.info("User has no keyboards");
                    return;
                }

                //Avoid NullPointerException
                if (response.body() == null || !response.body().hasSucceeded()) {
                    onFailure(call, new Exception("Problem while connecting to server"));
                    return;
                }

                //Log that the request was successful and add the keyboards to the list.
                List<Keyboard> keyboards = response.body().getKeyboards();
                Logger.info("Received user keyboards - there are " + keyboards.size() + " keyboards.");

                mKeyboardListAdapter.addAll(keyboards);
            }

            @Override
            public void onFailure(Call<GetAllUserKeyboardsResponse> call, Throwable t) {
                dismissDialog();
                Logger.error("Problem while trying to receive keyboards\n" + t.getMessage());

                Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                        .show();

                UIUtils.startActivity(getApplicationContext(), LoginActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //Set search bar hint text
        mSearchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        mSearchView.setQueryHint(getString(R.string.search_bar_hint));

        //Set search bar text and hint colors
        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(mContext, android.R.color.white));
        searchEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Start main menu on button click
            case R.id.main_menu_button:
                Intent mainMenu = new Intent(mContext, MenuActivity.class);
                startActivity(mainMenu);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        //If search bar is expanded, collapse it on back click
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return;
        }

        super.onBackPressed();
    }
}
