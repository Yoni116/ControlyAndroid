package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bowyer.app.fabtoolbar.FabToolbar;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.response.GetKeyboardLayoutResponse;
import net.controly.controly.http.service.KeyboardService;
import net.controly.controly.model.Keyboard;
import net.controly.controly.model.KeysLayout;
import net.controly.controly.util.EventCreationUtils;
import net.controly.controly.util.GraphicUtils;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;
import net.controly.controly.view.KeyView;
import net.controly.controly.view.KeyboardButton;
import net.controly.controly.view.OnDoubleClickListener;

import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the controller activity.
 */
public class KeyboardActivity extends BaseActivity {

    //-------Keyboard Data-------
    public static final String KEYBOARD_OBJECT_EXTRA = "KEYBOARD_DATA";
    private Keyboard mKeyboard;

    private Context mContext;
    private boolean mEditMode = false;

    //-------Views-------
    private RelativeLayout mControllerLayout;
    private FloatingActionButton mMenuFab;

    private HashSet<KeyboardButton> mKeyboardButtonsSet;

    //-------Fab Toolbars-------
    private FabToolbar mKeyboardFabToolbar;
    private FabToolbar mEditKeyboardFabToolbar;

    //These are the first drag points of a view
    private float firstDragX = 0;
    private float firstDragY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        mContext = this;

        //Get the keyboard data from the bundle object
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mKeyboard = (Keyboard) extras.getSerializable(KEYBOARD_OBJECT_EXTRA);
        }

        //This is the parent layout of the keyboard
        mControllerLayout = (RelativeLayout) findViewById(R.id.keyboard_key_layout);

        //This the set of keyboard buttons showing on screen
        mKeyboardButtonsSet = new HashSet<>();

        //Show keyboard menu on button click
        mMenuFab = (FloatingActionButton) findViewById(R.id.keyboard_menu_button);
        mKeyboardFabToolbar = (FabToolbar) findViewById(R.id.keyboard_toolbar);
        mKeyboardFabToolbar.setFab(mMenuFab);
        mMenuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeyboardFabToolbar.expandFab();
            }
        });

        mControllerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeyboardFabToolbar.contractFab();
            }
        });

        //On back button click - return to main activity
        ImageButton backButton = (ImageButton) findViewById(R.id.controller_menu_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainMenu();
            }
        });

        //When clicking on computer remote button, lunch the activity
        ImageButton computerRemoteButton = (ImageButton) findViewById(R.id.computer_remote_menu_button);
        computerRemoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ComputerRemoteActivity.class);
                startActivity(intent);
            }
        });

        //Connect button
        ImageButton connectButton = (ImageButton) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConnectDeviceActivity.class);
                startActivity(intent);
            }
        });

        //Initialize 'edit mode'
        mEditKeyboardFabToolbar = (FabToolbar) findViewById(R.id.edit_keyboard_toolbar);

        ImageButton enableEditButton = (ImageButton) findViewById(R.id.enable_edit_button);
        enableEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditMode();
            }
        });

        //Initialize the edit mode disable button
        ImageButton cancelEditButton = (ImageButton) findViewById(R.id.cancel_edit_button);
        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditMode();
            }
        });

        ImageButton applyEditButton = (ImageButton) findViewById(R.id.apply_edit_button);
        applyEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyEdit();
            }
        });

        ImageButton addKeyButton = (ImageButton) findViewById(R.id.add_key_button);
        addKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectDeviceActivity.class);
                intent.putExtra(EventCreationUtils.PURPOSE_EXTRA, SelectActionActivity.Purpose.KEY_CREATION);
                startActivity(intent);
            }
        });

        //On create load the layout
        loadLayout();
    }

    @Override
    public void onBackPressed() {

        //If keyboard toolbar is expanded, close it.
        if (mKeyboardFabToolbar.isFabExpanded()) {
            mKeyboardFabToolbar.contractFab();
        } else if (mEditMode) { //If edit mode is enabled, disable it.
            disableEditMode();
        } else { //Return back to the main menu.
            returnToMainMenu();
        }
    }

    private void returnToMainMenu() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    private void enableEditMode() {
        //Turn on edit mode
        mEditMode = true;

        //Show the edit toolbar
        mKeyboardFabToolbar.hide();
        mEditKeyboardFabToolbar.setFab(mMenuFab);
        mEditKeyboardFabToolbar.show();
    }

    private void disableEditMode() {
        mEditMode = false;

        mEditKeyboardFabToolbar.contractFab();

        for (KeyboardButton keyboardButton : mKeyboardButtonsSet) {
            float[] originalPosition = keyboardButton.getKeyPositionOnScreen();

            UIUtils.moveView(keyboardButton, originalPosition[0], originalPosition[1]);

            keyboardButton.setScaleX(1);
            keyboardButton.setScaleY(1);
        }
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
     * This method gets the response from the API and draws the layout according to it.
     *
     * @param response The response object.
     */
    private void drawLayout(GetKeyboardLayoutResponse response) {
        KeysLayout keysLayout = response.getKeysLayout();

        //If the response did not return any key layout
        if (keysLayout == null) {
            Logger.info("No key layout!");
            return;
        }

        for (KeyView keyView : response.getKeysLayout().getKeys()) {

            //Declare a new button and set its name
            final KeyboardButton button = new KeyboardButton(mContext, keyView, keysLayout.getScreenSize());

            float[] size = button.getKeySizeOnScreen();
            float[] position = button.getKeyPositionOnScreen();

            //When touching the button in edit mode, drag & drop the button.
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    //If we are not in edit mode, don't do anything.
                    if (!mEditMode) {
                        return false;
                    }

                    //This is the button that was clicked
                    KeyboardButton target = (KeyboardButton) view.getParent();
                    target.onTouchEvent(motionEvent);

                    //Start the drag & drop process
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            firstDragX = motionEvent.getX();
                            firstDragY = motionEvent.getY();
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            //This is the difference between the current touch point and the previous touch point.
                            float dx = motionEvent.getX() - firstDragX;
                            float dy = motionEvent.getY() - firstDragY;

                            //This is the point to move to.
                            float newX = target.getX() + dx;
                            float newY = target.getY() + dy;

                            UIUtils.moveView(target, newX, newY);

                            return true;
                    }

                    return false;
                }
            });

            button.setOnClickListener(new OnDoubleClickListener() {
                @Override
                public void onDoubleClick(final View view) {

                    if (!mEditMode) {
                        return;
                    }

                    int[] screenSize = GraphicUtils.getScreenSize(mContext);

                    //The position to move to
                    final float x = screenSize[0] / 2 - view.getWidth() / 2;
                    final float y = view.getHeight() / 8;

                    //The difference in position
                    final float dx = x - view.getX();
                    final float dy = y - view.getY();

                    TranslateAnimation animation = new TranslateAnimation(0, dx, 0, dy);
                    animation.setDuration(200);
                    animation.setFillAfter(false);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            UIUtils.moveView(view, x, y);
                        }
                    });

                    if (view.getAnimation() == null) {
                        view.startAnimation(animation);

                        float buttonsY = y + view.getHeight() + GraphicUtils.convertDpToPixels(mContext, 10);
                        float buttonMargin = GraphicUtils.convertDpToPixels(mContext, 150);

                        Button b1 = new Button(mContext);
                        UIUtils.drawView(mControllerLayout, b1, x - view.getWidth() / 2, buttonsY, 200, 200);

                        Button b2 = new Button(mContext);
                        UIUtils.drawView(mControllerLayout, b2, x - view.getWidth() / 2 + buttonMargin, buttonsY, 200, 200);

                        Button b3 = new Button(mContext);
                        UIUtils.drawView(mControllerLayout, b3, x - view.getWidth() / 2 + 2 * buttonMargin, buttonsY, 200, 200);

                        Button b4 = new Button(mContext);
                        UIUtils.drawView(mControllerLayout, b4, x - view.getWidth() / 2 + 3 * buttonMargin, buttonsY, 200, 200);
                    }
                }
            });

            //Add the new button to the layout
            UIUtils.drawView(mControllerLayout, button, position[0], position[1], (int) size[0], (int) size[1]);
            mKeyboardButtonsSet.add(button);
        }
    }

    private void applyEdit() {
        float[] widthHeightRatio;

        for (KeyboardButton keyboardButton : mKeyboardButtonsSet) {
            float[] size = keyboardButton.getSizeOnMakersScreen();
            float[] position = keyboardButton.getKeyPositionOnMakersScreen();
        }
    }
}
