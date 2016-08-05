package net.controly.controly.view;

import android.view.View;

/**
 * This is a listener for double clicking on a view.
 */
public abstract class OnDoubleClickListener implements View.OnClickListener {


    private static final long DOUBLE_CLICK_INTERVAL = 300;
    long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_INTERVAL) {
            onDoubleClick(view);
        }

        lastClickTime = clickTime;
    }

    public abstract void onDoubleClick(View v);
}
