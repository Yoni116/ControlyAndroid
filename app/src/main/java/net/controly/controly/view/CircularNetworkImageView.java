package net.controly.controly.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;

/**
 * Created by Itai on 27-Jul-16.
 */
public class CircularNetworkImageView extends LinearLayout {

    private NetworkImageView mNetworkImageView;

    public CircularNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularNetworkImageView);
        float radius = a.getDimension(R.styleable.CircularNetworkImageView_radius, 150);
        int backgroundColor = a.getColor(R.styleable.CircularNetworkImageView_circle_color, getResources().getColor(R.color.backgroundColor));

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_circular_network_image_view, this, true);

        CardView imageCircle = (CardView) getChildAt(0);
        mNetworkImageView = (NetworkImageView) imageCircle.getChildAt(0);

        imageCircle.setRadius(radius);
        imageCircle.setPreventCornerOverlap(false);
        imageCircle.getLayoutParams().height = (int) (2 * radius);
        imageCircle.getLayoutParams().width = (int) (2 * radius);
        imageCircle.setBackgroundColor(backgroundColor);
    }

    public void setImageUrl(String url) {
        ImageLoader imageLoader = ControlyApplication.getInstance()
                .getImageLoader();

        mNetworkImageView.setImageUrl(url, imageLoader);
    }
}
