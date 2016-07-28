package net.controly.controly.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import net.controly.controly.R;

/**
 * This view is a circular shaped network image view.
 */
public class CircularImageView extends LinearLayout {

    private ImageView mImageView;

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Get the XML attributes of the view: radius, color of the circular border, offline image (optional).
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView);
        float radius = a.getDimension(R.styleable.CircularImageView_radius, 150);
        int backgroundColor = a.getColor(R.styleable.CircularImageView_circle_color, getResources().getColor(R.color.backgroundColor));
        int src = a.getResourceId(R.styleable.CircularImageView_src, -1);

        a.recycle();

        //Inflate the layout of the view.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_circular_image_view, this, true);

        //Get the card view and the network image view instances.
        CardView imageCircle = (CardView) getChildAt(0);
        mImageView = (ImageView) imageCircle.getChildAt(0);

        //Set the color
        imageCircle.setCardBackgroundColor(backgroundColor);

        //Set the radius, width and height
        //The width and height are equal to the diameter of the circle
        imageCircle.setRadius(radius);
        imageCircle.getLayoutParams().height = (int) (2 * radius);
        imageCircle.getLayoutParams().width = (int) (2 * radius);

        if (src != -1) {
            mImageView.setImageResource(src);
        }
    }

    /**
     * This method receives a URL and sets the network image to the given URL.
     *
     * @param url URL of the image.
     */
    public void setImageUrl(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(mImageView);
    }

    /**
     * Set a bitmap to the circular image view.
     *
     * @param bitmap Bitmap to set.
     */
    public void setImageBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }
}
