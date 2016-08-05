package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
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
import net.controly.controly.view.OnDoubleClickListener;

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
    private boolean mEditMode = false;

    //-------Views-------
    private RelativeLayout mControllerLayout;
    private FrameLayout mMenuButtonContainer; //We need this view since there is no other way to make a FloatingActionButton invisible.
    private FloatingActionButton mMenuButton;

    //-------Menus-------
    private View mControllerMenu;
    private View mEditMenu;

    //-------Keyboard Menu-------
    private FloatingActionButton mBackButton;
    private FloatingActionButton mComputerRemoteButton;
    private FloatingActionButton mEnableEditButton;

    //-------Edit Menu-------
    private FloatingActionButton mDisableEditButton;

    //These are the first drag points of a view
    private float firstDragX = 0;
    private float firstDragY = 0;

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

        mControllerLayout = (RelativeLayout) findViewById(R.id.keyboard_key_layout);
        mControllerMenu = findViewById(R.id.keyboard_menu);

        mEditMenu = findViewById(R.id.edit_menu);

        //Show controller menu on button click
        mMenuButton = (FloatingActionButton) findViewById(R.id.keyboard_menu_button);
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

        mMenuButtonContainer = (FrameLayout) findViewById(R.id.menu_button_container);

        //On back button click - return to main activity
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
                Intent intent = new Intent(mContext, ComputerRemoteActivity.class);
                startActivity(intent);
            }
        });

        mEnableEditButton = (FloatingActionButton) findViewById(R.id.enable_edit_button);
        mEnableEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnEditMode();
            }
        });

        mDisableEditButton = (FloatingActionButton) findViewById(R.id.disable_edit_button);
        mDisableEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffEditMode();
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
     * When edit mode is turned on, the menu floating action button and the keyboard menu will disappear.
     * Instead, the keyboard edit menu will appear.
     */
    private void turnOnEditMode() {
        Logger.info("Enabling edit mode");
        mEditMode = true;

        mMenuButtonContainer.setVisibility(View.GONE);
        mControllerMenu.setVisibility(View.GONE);

        mEditMenu.setVisibility(View.VISIBLE);
    }

    /**
     * When edit mode is turned on, the menu floating action button and the keyboard menu will disappear.
     * Instead, the keyboard edit menu will appear.
     */
    private void turnOffEditMode() {
        Logger.info("Disabling edit mode");
        mEditMode = false;

        mMenuButtonContainer.setVisibility(View.VISIBLE);
        mControllerMenu.setVisibility(View.VISIBLE);

        mEditMenu.setVisibility(View.GONE);
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
                    if (!mEditMode) {
                        return false;
                    }

                    //This is the button that was clicked
                    KeyboardButton target = ((KeyboardButton) view.getParent());
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

                        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = vi.inflate(R.layout.key_quick_action_menu, null);

                        ((ViewGroup) view).addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }
            });

            //Add the new button to the layout
            UIUtils.drawView(mControllerLayout, button, x, y, width, height);
        }
    }
}
