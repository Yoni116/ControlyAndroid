package net.controly.controly.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * This class contains an assortment of util methods for graphics.
 */
public class GraphicUtils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixels(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Convert a bitmap to byte array.
     *
     * @param bitmap The bitmap to convert.
     * @return A byte array from the given bitmap.
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);

        return stream.toByteArray();
    }

    /**
     * Get the size of the screen.
     * The first value of the array is the width, and the second one is the height.
     *
     * @param context Context of the application.
     * @return Screen size in an array - first value is width, and second value is height.
     */
    public static int[] getScreenSize(Context context) {
        Point size = new Point();

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getSize(size);

        return new int[]{size.x, size.y};
    }

    /**
     * Get the bitmap from an {@link ImageView}.
     *
     * @param imageView Image view to get bitmap from.
     * @return The bitmap of an {@link ImageView}.
     */
    public static Bitmap getBitmap(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    /**
     * Rotate the given bitmap by 90 degrees.
     *
     * @param bitmap The bitmap to rotate_clockwise.
     * @return The rotated bitmap.
     */
    public static Bitmap rotate(Bitmap bitmap) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.preRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);
    }

    /**
     * @return The maximum size of a texture.
     */
    private static int getMaxTextureSize() {
        Canvas canvas = new Canvas();
        return Math.max(canvas.getMaximumBitmapHeight(), canvas.getMaximumBitmapWidth()) / 8;
    }

    /**
     * In each Android device there's a maximum texture size according to the device's gpu and screen size.
     * This method finds the maximum texture size, and preservers the size of the given bitmap so it doesn't exceed that size.
     *
     * @param bitmap The bitmap to preserver.
     * @return The preserved bitmap.
     */
    public static Bitmap preserveMaxDimensions(Bitmap bitmap) {
        int maxTextureSize = getMaxTextureSize();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width < maxTextureSize && height < maxTextureSize) {
            return bitmap;
        }

        float factor = width > height ? (float) (maxTextureSize) / (float) (width) : (float) (maxTextureSize) / (float) (height);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (factor * width), (int) (factor * height), false);

        return bitmap;
    }
}
