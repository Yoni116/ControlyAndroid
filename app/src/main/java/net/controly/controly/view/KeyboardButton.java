package net.controly.controly.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.controly.controly.R;
import net.controly.controly.model.Key;

/**
 * This view represents a circular keyboard button.
 */
public class KeyboardButton extends LinearLayout {

    private Key mKey;
    private Context mContext;

    //-------Views-------
    private RelativeLayout mKeyView; //The shape drawable
    private RelativeLayout mKeyBackground; //The key color
    private ImageView mKeyIcon; //The icon of the key
    private TextView mKeyName; //The key text

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1f;

    private GestureDetector mGestureDetector;

    public KeyboardButton(Context context) {
        super(context);
    }

    public KeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Initialize a new keyboard button.
     *
     * @param context The context of the application.
     * @param key     The button data.
     */
    public KeyboardButton(Context context, Key key) {
        super(context);

        this.mContext = context;
        this.mKey = key;

        initializeButton();
    }

    /**
     * Initialize the new button layout.
     */
    private void initializeButton() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.view_keyboard_button, this);

        mKeyView = (RelativeLayout) this.findViewById(R.id.key_shape_drawable);
        mKeyBackground = (RelativeLayout) this.findViewById(R.id.key_icon_color);
        mKeyIcon = (ImageView) this.findViewById(R.id.key_icon_drawable);
        mKeyName = (TextView) this.findViewById(R.id.key_name);

        //Set the button layout if it's a rectangle.
        if (mKey.getKeyType() == Key.KeyType.RECTANGLE) {
            StateListDrawable drawable = (StateListDrawable) mKeyView.getBackground();
            Drawable[] drawables = ((DrawableContainer.DrawableContainerState) drawable.getConstantState()).getChildren();

            GradientDrawable selected = (GradientDrawable) drawables[0].mutate();
            GradientDrawable notSelected = (GradientDrawable) drawables[1].mutate();

            selected.setShape(GradientDrawable.RECTANGLE);
            selected.setCornerRadius(15);

            selected.invalidateSelf();

            notSelected.setShape(GradientDrawable.RECTANGLE);
            notSelected.setCornerRadius(15);

            notSelected.invalidateSelf();
        }

        //Set the button name.
        if (mKey.getName() != null) {
            setKeyName(mKey.getName());
        }

        //Set the button color.
        if (mKey.getHexColor() != null) {
            setKeyColor(mKey.getColor());
        }

        //Set the button text.
        if (mKey.getIconName() != null) {
            setKeyIcon(mKey.getIconName());
        }

        mScaleDetector = new ScaleGestureDetector(mContext, new ScaleListener());
        mGestureDetector = new GestureDetector(mContext, new GestureListener());
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
        mKeyView.setOnLongClickListener(l);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mKeyView.setOnClickListener(l);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        mKeyView.setOnTouchListener(l);
    }

    @Override
    public void setOnDragListener(OnDragListener l) {
        super.setOnDragListener(l);
        mKeyView.setOnDragListener(l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * This method receives the button name and sets it to the button.
     *
     * @param keyName The name of the key.
     */
    private void setKeyName(String keyName) {
        mKeyName.setText(keyName);

        if (mKeyName.getText().equals("")) {
            mKeyName.setVisibility(GONE);
        } else {
            mKeyName.setVisibility(VISIBLE);
        }
    }

    /**
     * This method receives a color and sets it to the button.
     *
     * @param color The color of the key.
     */
    private void setKeyColor(int color) {
        mKeyName.setTextColor(color);
        mKeyBackground.setBackgroundColor(color);
    }

    /**
     * This method receives an icon name, and sets it to the key.
     *
     * @param iconName The name of the icon.
     */
    private void setKeyIcon(String iconName) {
        //Change the name to the format in the Android platform.
        iconName = iconName.toLowerCase()
                .replace("-", "_");

        mKeyIcon.setImageResource(getResources().getIdentifier(iconName, "drawable", mContext.getPackageName()));
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            setScaleX(mScaleFactor);
            setScaleY(mScaleFactor);
            invalidate();

            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(mContext, "Edit mode", Toast.LENGTH_SHORT).show();
            return super.onDoubleTap(e);
        }
    }
}
