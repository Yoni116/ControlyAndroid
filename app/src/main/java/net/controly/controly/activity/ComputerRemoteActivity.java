package net.controly.controly.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import net.controly.controly.R;

/**
 * This activity is for controlling pc remotely.
 */
public class ComputerRemoteActivity extends BaseActivity {

    private TextView mLastTypedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_remote);

        mLastTypedText = (TextView) findViewById(R.id.last_typed_text);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected boolean enableImmersive() {
        return false;
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
