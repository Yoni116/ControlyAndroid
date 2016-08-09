package net.controly.controly.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.controly.controly.R;

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

    boolean enableImmersiveMode() {
        return true;
    }
}
