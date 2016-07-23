package net.controly.controly.activity;

import android.os.Bundle;
import android.os.Handler;

import net.controly.controly.R;

/**
 * This is the splash activity of the application.
 */
public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DURATION = 1200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //TODO: If user is already authenticated, continue to the main activity.
        startAuthenticationFlow();
    }

    /**
     * If the user is logged in already, continue to the main activity.
     * If not, continue to the login activity.
     */
    private void startAuthenticationFlow() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
