package net.controly.controly.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

/**
 * This class defines the base activity of the application.
 */
public abstract class BaseActivity extends AppCompatActivity {

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
    }
}
