package net.controly.controly.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import net.controly.controly.R;

/**
 * Created by Itai on 03-Aug-16.
 */
public class ActivityComputerRemote extends BaseActivity {

    private TextView mLastTypedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_remote);

        mLastTypedText = (TextView) findViewById(R.id.last_typed_text);
    }

    @Override
    protected boolean enableImmersive() {
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            String character = Character.toString((char) event.getUnicodeChar(event.getMetaState()));
            mLastTypedText.setText(character);
        }

        return super.dispatchKeyEvent(event);
    }
}
