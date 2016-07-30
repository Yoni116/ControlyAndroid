package net.controly.controly.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.controly.controly.R;

/**
 * Created by Itai on 30-Jul-16.
 */
public class ControllerActivity extends BaseActivity {

    private Context mContext;

    private FloatingActionButton mMenuButton;
    private View mControllerMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        mContext = this;

        mControllerMenu = findViewById(R.id.controller_menu);

        mMenuButton = (FloatingActionButton) findViewById(R.id.controller_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation = null;
                int visibility = View.VISIBLE;

                switch (mControllerMenu.getVisibility()) {
                    case View.VISIBLE:
                        animation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_down);
                        visibility = View.GONE;
                        break;
                    case View.GONE:
                        animation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);
                        visibility = View.VISIBLE;
                        mMenuButton.setSelected(true);
                        break;
                }

                mControllerMenu.startAnimation(animation);
                mControllerMenu.setVisibility(visibility);
            }
        });
    }
}
