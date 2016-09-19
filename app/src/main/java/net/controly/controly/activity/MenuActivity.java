package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import net.controly.controly.R;

/**
 * This activity is for the menu of the application.
 */
public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Context context = this;

        //Initialize the logout button
        ImageView settingsButton = (ImageView) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsActivity = new Intent(context, SettingsActivity.class);
                startActivity(settingsActivity);
            }
        });

        ImageView createKeyboardButton = (ImageView) findViewById(R.id.create_keyboard_button);
        createKeyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateKeyboardActivity.class);
                startActivity(intent);
            }
        });

        ImageView eventsButton = (ImageView) findViewById(R.id.events_button);
        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventsActivity.class);
                startActivity(intent);
            }
        });

        ImageView myLocationsButton = (ImageView) findViewById(R.id.my_locations_button);
        myLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyLocationsActivity.class);
                startActivity(intent);
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
