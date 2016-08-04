package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.response.GetKeyboardLayoutResponse;
import net.controly.controly.http.service.KeyboardService;
import net.controly.controly.model.Key;
import net.controly.controly.model.Keyboard;
import net.controly.controly.util.GraphicUtils;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;
import net.controly.controly.view.KeyboardButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the controller activity.
 */
public class ControllerActivity extends BaseActivity {

    public static final String CONTROLLER_OBJECT_EXTRA = "CONTROLLER_ID";
    private Keyboard mKeyboard;

    private Context mContext;
    private boolean editMode = false;

    //-------Views-------
    private RelativeLayout mControllerLayout;
    private View mControllerMenu;
    private FloatingActionButton mMenuButton;

    //-------Controller Menu-------
    private FloatingActionButton mBackButton;
    private FloatingActionButton mComputerRemoteButton;
    private FloatingActionButton mEditLayoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        mContext = this;

        //Get the keyboard id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mKeyboard = (Keyboard) extras.getSerializable(CONTROLLER_OBJECT_EXTRA);
        }

        mControllerLayout = (RelativeLayout) findViewById(R.id.controller_key_layout);
        mControllerMenu = findViewById(R.id.controller_menu);

        //Show controller menu on button click
        mMenuButton = (FloatingActionButton) findViewById(R.id.controller_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mControllerMenu.getVisibility()) {
                    //If the menu is visible, hide it.
                    case View.VISIBLE:
                        hideControllerMenu();
                        break;

                    //If the menu is gone, show it.
                    case View.GONE:
                        showControllerMenu();
                        break;
                }
            }
        });

        //On back button click return to main activity
        mBackButton = (FloatingActionButton) findViewById(R.id.controller_menu_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mComputerRemoteButton = (FloatingActionButton) findViewById(R.id.computer_remote_menu_button);
        mComputerRemoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityComputerRemote.class);
                startActivity(intent);
            }
        });

        mEditLayoutButton = (FloatingActionButton) findViewById(R.id.edit_layout_button);
        mEditLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = !editMode;
                Logger.info("Edit mode enabled: " + editMode);
            }
        });

        //On create load the layout
        loadLayout();
    }

    /**
     * Show the controller menu.
     */
    private void showControllerMenu() {
        Animation jumpInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);
        Animation rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_clockwise);

        mControllerMenu.startAnimation(jumpInAnimation);
        mMenuButton.startAnimation(rotateAnimation);
        mControllerMenu.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the controller menu.
     */
    private void hideControllerMenu() {
        Animation jumpInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_down);
        Animation rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_counter_clockwise);

        mControllerMenu.startAnimation(jumpInAnimation);
        mMenuButton.startAnimation(rotateAnimation);
        mControllerMenu.setVisibility(View.GONE);
    }

    /**
     * Load the controller layout.
     */
    private void loadLayout() {
        showWaitDialog();

        Call<GetKeyboardLayoutResponse> call = ControlyApplication.getInstance()
                .getService(KeyboardService.class)
                .getKeyboardLayout(mKeyboard.getId());

        call.enqueue(new Callback<GetKeyboardLayoutResponse>() {
            @Override
            public void onResponse(Call<GetKeyboardLayoutResponse> call, Response<GetKeyboardLayoutResponse> response) {
                dismissDialog();

                GetKeyboardLayoutResponse responseBody = response.body();

                //If the request was not successful
                if (!responseBody.hasSucceeded()) {
                    onFailure(call, null);
                    return;
                }

                drawLayout(responseBody);
            }

            @Override
            public void onFailure(Call<GetKeyboardLayoutResponse> call, Throwable t) {
                dismissDialog();

                Logger.error("Error while trying to retrieve keyboard");
                if (t != null) {
                    Logger.error(t.getMessage());
                }

                Toast.makeText(mContext, "There was an error while trying to retrieve keyboard", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * This method gets the {@link GetKeyboardLayoutResponse} and draws the keyboard layout according to it.
     *
     * @param response The response object.
     */
    private void drawLayout(GetKeyboardLayoutResponse response) {
        int[] actualScreenSize = GraphicUtils.getScreenSize(mContext);
        int[] makersScreenSize = response.getKeysLayout().getScreenSize();

        //Calculate the ratios between maker's screen size and actual screen size.
        float widthRatio = ((float) (actualScreenSize[0])) / ((float) (makersScreenSize[0]));
        float heightRatio = ((float) (actualScreenSize[1])) / ((float) (makersScreenSize[1]));

        for (Key key : response.getKeysLayout().getKeys()) {

            //Declare a new button and set its name
            final KeyboardButton button = new KeyboardButton(mContext, key);

            //The width and height of the button
            int width, height;

            //Set the key's background and size according to its type
            if (key.getKeyType() == Key.KeyType.CIRCLE || key.getKeyType() == Key.KeyType.COLOR_PAD
                    || key.getKeyType() == Key.KeyType.LEVEL_CONTROL_KEY) {
                //Set the width and height of the new button
                width = (int) (key.getWidth() * Math.min(heightRatio, widthRatio));
                height = (int) (key.getHeight() * Math.min(heightRatio, widthRatio));
            } else {
                width = (int) (key.getWidth() * widthRatio);
                height = (int) (key.getHeight() * heightRatio);
            }

            //Set the position of the new button
            int x = (int) (key.getX() * widthRatio) - (width / 2);
            int y = (int) (key.getY() * heightRatio) - (height / 2);

            //When touching the button in edit mode, drag & drop the button.
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    //If we are not in edit mode, don't do anything.
                    if (!editMode) {
                        return false;
                    }

                    //Start the drag & drop process
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ((KeyboardButton) view.getParent()).setVisibility(View.INVISIBLE);

                        View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                        view.startDrag(null, shadow, view.getParent(), 0);

                        return true;
                    }

                    return false;
                }
            });

            //Add the new button to the layout
            UIUtils.drawView(mControllerLayout, button, x, y, width, height);
        }

        mControllerLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                //In case of drop - change the position of the button.
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        KeyboardButton view = (KeyboardButton) event.getLocalState();
                        view.setVisibility(View.VISIBLE);

                        mControllerLayout.removeView(view);
                        mControllerLayout.invalidate();

                        int x = (int) (event.getX() - view.getWidth() / 2);
                        int y = (int) (event.getY() - view.getHeight() / 2);

                        UIUtils.drawView(mControllerLayout, view, x, y, view.getWidth(), view.getHeight());
                        break;
                }

                return true;
            }
        });
    }
}
