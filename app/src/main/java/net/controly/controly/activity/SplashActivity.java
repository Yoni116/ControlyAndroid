package net.controly.controly.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.util.UIUtils;

/**
 * This is the splash activity of the application.
 */
public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DURATION = 1200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Context context = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Class nextActivity;

                //If the user is already authenticated, continue to the main activity.
                //If he is not authenticated, continue to the login activity.
                if (ControlyApplication.getInstace().isAuthenticated()) {
                    nextActivity = MainActivity.class;
                } else {
                    nextActivity = LoginActivity.class;
                }

                UIUtils.startActivity(context, nextActivity);
            }
        }, SPLASH_DURATION);
    }
}
