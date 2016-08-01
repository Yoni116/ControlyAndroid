package net.controly.controly.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * This class contains an assortment of util methods for graphics.
 */
public class GraphicUtils {

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
     * @param bitmap  The bitmap to rotate_clockwise.
     * @param degrees Degrees to rotate_clockwise by.
     * @return The rotated bitmap.
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.preRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);
    }

    /**
     * @return The maximum size of a texture.
     */
    public static int getMaxTextureSize() {
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
