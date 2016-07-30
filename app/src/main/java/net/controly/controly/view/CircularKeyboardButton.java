package net.controly.controly.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import net.controly.controly.R;

/**
 * Created by Itai on 30-Jul-16.
 */
public class CircularKeyboardButton extends LinearLayout {

    private Button mButton;
    private GradientDrawable mCircle;

    public CircularKeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Get the XML attributes of the view: keyboard name, radius, text color and keyboard icon.
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularKeyboardButton);
        String keyboardName = a.getString(R.styleable.CircularKeyboardButton_keyboardName);
        float radius = a.getDimension(R.styleable.CircularKeyboardButton_radius, 150);
        int textColor = a.getColor(R.styleable.CircularKeyboardButton_textColor, ContextCompat.getColor(context, R.color.background_color));
        int keyboardIcon = a.getResourceId(R.styleable.CircularKeyboardButton_keyboardIcon, -1);

        a.recycle();

        //Inflate the layout of the view.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_circular_keyboard_button, this, true);

        //Get the card view and the network image view instances.
        mButton = (Button) getChildAt(0);

        mButton.setText(keyboardName);
        mButton.setWidth((int) (radius * 2));
        mButton.setWidth((int) (radius * 2));
        mButton.setTextColor(textColor);

        mCircle = (GradientDrawable) mButton.getBackground();
        mCircle.setCornerRadius(radius);
        mCircle.setSize((int) (2 * radius), (int) (2 * radius));
    }
}
