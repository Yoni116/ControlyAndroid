package net.controly.controly.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * This class contains an assortment of util methods for bitmaps.
 */
public class BitmapUtils {

    /**
     * Rotate the given bitmap by 90 degress.
     *
     * @param bitmap The bitmap to rotate.
     * @return The rotated bitmap.
     */
    public static Bitmap rotate(Bitmap bitmap) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(90);

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
