package net.controly.controly.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.controly.controly.R;
import net.controly.controly.util.FontUtils;

import java.util.ArrayList;

/**
 * This class defines the base activity of the application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mWaitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set orientation to portrait only.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Set the immersive mode - hide navigation and status bar.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && enableImmersiveMode()) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * Show a progress dialog with the text "Loading" in the current activity.
     */
    final void showWaitDialog() {
        final String message = getString(R.string.message_loading);

        mWaitDialog = new ProgressDialog(this);
        mWaitDialog.setMessage(message);
        mWaitDialog.setCancelable(true);
        mWaitDialog.setIndeterminate(true);
        mWaitDialog.show();
    }

    /**
     * Dismiss the progress dialog if shown.
     */
    final void dismissDialog() {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * Show the back button on the toolbar.
     */
    final void showBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Configure the toolbar to the color of the app.
     */
    final void configureToolbar(boolean backButton, boolean light) {

        //Set background and title colors according to the toolbar theme.
        int backgroundColor = light ? Color.WHITE : Color.DKGRAY;
        int titleColor = light ? Color.DKGRAY : Color.WHITE;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(titleColor);
        toolbar.setBackgroundColor(backgroundColor);

        setSupportActionBar(toolbar);

        //Show back button if enabled.
        if (backButton) {
            showBackButton();
        }

        //Change toolbar font
        TextView titleView = getToolbarTextView();
        if (titleView != null) {
            titleView.setTextColor(titleColor);
            FontUtils.setTextViewFont(titleView);
        }
    }

    /**
     * @return The text view that the toolbar contains.
     */
    private TextView getToolbarTextView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //If we defined a custom textview in the toolbar, return it.
        TextView customTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (customTextView != null) {
            customTextView.setText(toolbar.getTitle());
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            return customTextView;
        }

        //Return the default textview in the toolbar.
        ArrayList<View> titleViews = new ArrayList<>(1);
        toolbar.findViewsWithText(titleViews, toolbar.getTitle().toString(), View.FIND_VIEWS_WITH_TEXT);

        if (!titleViews.isEmpty()) {
            return (TextView) titleViews.get(0);
        }

        return null;
    }

    boolean enableImmersiveMode() {
        return true;
    }
}
