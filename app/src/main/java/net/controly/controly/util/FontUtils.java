package net.controly.controly.util;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * This class is used to override the default font of the application.
 */
public final class FontUtils {

    /**
     * Set the default font of the app.
     *  @param context                 The context of the current activity.
     * @param fontAssetName           The name of the font to override.
     */
    public static void setDefaultFont(Context context,
                                      String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(regular);
    }

    /**
     * This method uses reflection to override the default font type.
     *
     * @param newTypeface             The new type face.
     */
    private static void replaceFont(final Typeface newTypeface) {
        try {

            final Field staticField = Typeface.class.getDeclaredField("MONOSPACE");
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
