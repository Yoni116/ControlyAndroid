package net.controly.controly.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.controly.controly.R;

/**
 * This view represents a circular keyboard button.
 */
public class CircularKeyboardButton extends LinearLayout {

    private RelativeLayout mCircleView;
    private RelativeLayout mKeyBackground;
    private ImageView mKeyIcon;
    private TextView mKeyName;

    public CircularKeyboardButton(Context context) {
        super(context);

        initializeButton();
    }

    public CircularKeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflateLayout();
    }

    private void inflateLayout() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.view_circular_keyboard_button, this);
    }

    private void initializeButton() {
/*        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularKeyboardButton);

        String keyName = a.getString(R.styleable.CircularKeyboardButton_keyName);
        int keyColor = a.getColor(R.styleable.CircularKeyboardButton_keyColor, ContextCompat.getColor(getContext(), R.color.color_accent));
        int keyboardIcon = a.getResourceId(R.styleable.CircularKeyboardButton_keyIcon, 0); //TODO Use null drawable

        a.recycle();*/

        LayoutInflater.from(getContext())
                .inflate(R.layout.view_circular_keyboard_button, this);

        mCircleView = (RelativeLayout) this.findViewById(R.id.key_circle_view);
        mKeyBackground = (RelativeLayout) this.findViewById(R.id.key_icon_background);
        mKeyIcon = (ImageView) this.findViewById(R.id.key_icon_drawable);
        mKeyName = (TextView) this.findViewById(R.id.key_name);

/*        if (keyName != null) {
            mKeyName.setText(keyName);
            mKeyName.setTextColor(keyColor);
        }

        if (keyboardIcon != 0) {
            mKeyIcon.setImageResource(keyboardIcon);
            mKeyBackground.setBackgroundColor(keyColor);
        }*/
    }

    public void setKeyName(String keyName) {
        mKeyName.setText(keyName);
    }

    public void setKeyColor(int color) {
        mKeyName.setTextColor(color);
        mKeyBackground.setBackgroundColor(color);
    }

    public void setKeyIcon(int iconResId) {
        mKeyIcon.setImageResource(iconResId);
    }

    public void setSize(int width, int height) {
        mCircleView.getLayoutParams().height = height;
        mCircleView.getLayoutParams().width = width;
    }
}
