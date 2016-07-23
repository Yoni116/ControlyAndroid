package net.controly.controly.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.controly.controly.R;

/**
 * This class defines the base activity of the application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mWaitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Starts the given activity.
     *
     * @param activityClass The activity to start.
     */
    public void startActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);

        if (Build.VERSION.SDK_INT >= 11) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(intent);
        finish();
    }

    /**
     * Show a progress dialog with the text "Loading" in the current activity.
     */
    public final void showWaitDialog() {
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
    public final void dismissDialog() {

        if(mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }
}
