package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.util.Logger;
import net.controly.controly.util.UIUtils;

/**
 * This activity is for the menu of the application.
 */
public class MenuActivity extends BaseActivity {

    private FloatingActionButton mLogoutButton;
    private FloatingActionButton mSettingsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Context context = this;

        //Initialize the logout button
        //TODO Change icon
        mLogoutButton = (FloatingActionButton) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.info("Logging out of the application");
                ControlyApplication.getInstance()
                        .logout(context);

                UIUtils.startActivity(context, LoginActivity.class);
            }
        });

        mSettingsButton = (FloatingActionButton) findViewById(R.id.settings_button);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsActivity = new Intent(context, SettingsActivity.class);
                startActivity(settingsActivity);
            }
        });
    }

    /**
     * When the user clicks the activity, close the menu.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            finish();
            return true;
        }

        return false;
    }
}
