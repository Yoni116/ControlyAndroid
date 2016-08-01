package net.controly.controly.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

    //-------Views-------
    private RelativeLayout mControllerLayout;
    private View mControllerMenu;
    private FloatingActionButton mMenuButton;

    private FloatingActionButton mBackButton;

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

                Animation jumpInAnimation = null;
                Animation rotateAnimation = null;
                int visibility = View.VISIBLE;

                switch (mControllerMenu.getVisibility()) {

                    //If the menu is visible, hide it.
                    case View.VISIBLE:
                        jumpInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_down);
                        rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_counter_clockwise);
                        visibility = View.GONE;

                        break;

                    //If the menu is gone, show it.
                    case View.GONE:
                        jumpInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);
                        rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_clockwise);
                        visibility = View.VISIBLE;

                        break;
                }

                //Keep the button rotated.
                assert rotateAnimation != null;
                rotateAnimation.setFillAfter(true);

                mControllerMenu.startAnimation(jumpInAnimation);
                mMenuButton.startAnimation(rotateAnimation);
                mControllerMenu.setVisibility(visibility);
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

        loadLayout();
    }

    /**
     * Show the controller menu.
     * TODO Implement.
     */
    private void showControllerMenu() {
    }

    /**
     * Hide the controller menu.
     * TODO Implement and call on create.
     */
    private void hideControllerMenu() {
    }

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
            Button button = new Button(mContext);
            button.setText(key.getName());

            int width = (int) (key.getWidth() * widthRatio);
            int height = (int) (key.getHeight() * heightRatio);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            params.leftMargin = (int) (key.getX() * widthRatio) - (width / 2);
            params.topMargin = (int) (key.getY() * heightRatio) - (height / 2);

            mControllerLayout.addView(button, params);
        }
    }
}
