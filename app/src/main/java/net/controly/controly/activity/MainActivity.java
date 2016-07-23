package net.controly.controly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.controly.controly.R;

public class MainActivity extends BaseActivity {

    private FloatingActionButton mMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuButton = (FloatingActionButton) findViewById(R.id.open_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });
    }

}
