package net.controly.controly.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import net.controly.controly.R;

/**
 * This activity is for the menu of the application.
 */
public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
}
