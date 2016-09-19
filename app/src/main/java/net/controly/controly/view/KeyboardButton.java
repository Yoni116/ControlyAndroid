package net.controly.controly.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.controly.controly.R;
import net.controly.controly.util.GraphicUtils;

/**
 * This view represents a circular keyboard button.
 */
public class KeyboardButton extends LinearLayout {

    private Context mContext;
    private KeyView mKey;
    private int[] mMakersScreenSize;

    //-------Views-------
    private RelativeLayout mKeyView; //The shape drawable
    private RelativeLayout mKeyBackground; //The key color
    private ImageView mKeyIcon; //The icon of the key
    private TextView mKeyName; //The key text

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1f;

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
     * @param keyView The keyView to draw.
     */
    public KeyboardButton(Context context, KeyView keyView) {
        super(context);

        this.mContext = context;
        this.mKey = keyView;

        initializeButton();
    }

    /**
     * Initialize a new keyboard button.
     *
     * @param context      The context of the application.
     * @param keyView      The keyView to draw.
     * @param makersScreen The screen size of the person who created the keyView button.
     */
    public KeyboardButton(Context context, KeyView keyView, int[] makersScreen) {
        this(context, keyView);
        this.mMakersScreenSize = makersScreen;
    }

    /**
     * Initialize the new button layout.
     */
    private void initializeButton() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_keyboard_button, this);

        mKeyView = (RelativeLayout) this.findViewById(R.id.key_shape_drawable);
        mKeyBackground = (RelativeLayout) this.findViewById(R.id.key_icon_color);
        mKeyIcon = (ImageView) this.findViewById(R.id.key_icon_drawable);
        mKeyName = (TextView) this.findViewById(R.id.key_name);

        //Set the button layout if it's a rectangle.
        if (mKey.getKeyType() == KeyView.KeyType.RECTANGLE) {
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
    }

    /**
     * @return The key data.
     */
    public KeyView getKeyButton() {
        return mKey;
    }

    /**
     * @return The maker's screen size.
     */
    public int[] getMakersScreenSize() {
        return mMakersScreenSize;
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

    /**
     * This method calculates the position that the key should be put on the user's screen.
     *
     * @return The first element of the array is the X coordinate, and the second element of the array is the Y coordinate.
     */
    public float[] getKeyPositionOnScreen() {
        float[] widthAndHeightRatio = getWidthAndHeightRatio();
        float[] size = getKeySizeOnScreen();

        float x = (int) (mKey.getX() * widthAndHeightRatio[0]) - (size[0] / 2);
        float y = (int) (mKey.getY() * widthAndHeightRatio[1]) - (size[1] / 2);

        return new float[]{x, y};
    }

    /**
     * This method calculates the key's position relative to the maker's screen.
     *
     * @return The first element of the array is the X coordinate, and the second element of the array is the Y coordinate.
     */
    public float[] getKeyPositionOnMakersScreen() {
        float[] widthAndHeightRatio = getWidthAndHeightRatio();
        float[] size = getKeySizeOnScreen();

        float[] currPosition = getKeyPositionOnScreen();

        float x = (int) (currPosition[0] + (size[0] / 2)) / widthAndHeightRatio[0];
        float y = (int) (currPosition[1] + (size[1] / 2)) / widthAndHeightRatio[1];

        return new float[]{x, y};
    }

    /**
     * This method calculates the size of the keyboard button considering the user's screen size.
     *
     * @return The first element of the array is the width of the button, and the second element is height of the button.
     */
    public float[] getKeySizeOnScreen() {
        float[] widthAndHeightRatio = getWidthAndHeightRatio();

        //The width and height of the button
        float width, height;

        //Set the key's size according to its type
        if (mKey.getKeyType() == KeyView.KeyType.CIRCLE || mKey.getKeyType() == KeyView.KeyType.COLOR_PAD
                || mKey.getKeyType() == KeyView.KeyType.LEVEL_CONTROL_KEY) {
            //Set the width and height of the new button
            width = (int) (mKey.getWidth() * Math.min(widthAndHeightRatio[0], widthAndHeightRatio[1]));
            height = (int) (mKey.getHeight() * Math.min(widthAndHeightRatio[0], widthAndHeightRatio[1]));
        } else {
            width = (int) (mKey.getWidth() * widthAndHeightRatio[0]);
            height = (int) (mKey.getHeight() * widthAndHeightRatio[1]);
        }

        return new float[]{width, height};
    }

    /**
     * Converts the actual size of the key, to the size relative to the maker's screen.
     *
     * @return The first element of the array is the width of the button, and the second element is height of the button.
     */
    public float[] getSizeOnMakersScreen() {
        float[] widthAndHeightRatio = getWidthAndHeightRatio();
        float width, height;

        //Get the key's size according to its type.
        if (mKey.getKeyType() == KeyView.KeyType.CIRCLE || mKey.getKeyType() == KeyView.KeyType.COLOR_PAD
                || mKey.getKeyType() == KeyView.KeyType.LEVEL_CONTROL_KEY) {
            width = (mKeyView.getWidth() / Math.max(widthAndHeightRatio[0], widthAndHeightRatio[1]));
            height = (mKeyView.getHeight() / Math.max(widthAndHeightRatio[0], widthAndHeightRatio[1]));
        } else {
            width = (mKeyView.getWidth() / widthAndHeightRatio[0]);
            height = (mKeyView.getHeight() / widthAndHeightRatio[1]);
        }

        return new float[]{width, height};
    }

    /**
     * This method calculates the ratio between the width and height of the screen size of the
     * person who created the key button and of the actual user screen.
     *
     * @return The first element of the array is the width ratio, and the second element is the height ratio.
     */
    private float[] getWidthAndHeightRatio() {

        int[] actualScreenSize = GraphicUtils.getScreenSize(mContext);

        //Calculate the ratio between maker's screen size and actual screen size.
        float widthRatio = ((float) (actualScreenSize[0])) / ((float) (mMakersScreenSize[0]));
        float heightRatio = ((float) (actualScreenSize[1])) / ((float) (mMakersScreenSize[1]));

        return new float[]{widthRatio, heightRatio};
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            //TODO Maybe we should try to find a way to actually change the view size instead of scaling it.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            setScaleX(mScaleFactor);
            setScaleY(mScaleFactor);
            invalidate();

            return true;
        }
    }
}
