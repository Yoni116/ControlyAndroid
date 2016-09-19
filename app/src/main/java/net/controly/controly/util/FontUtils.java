package net.controly.controly.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import net.controly.controly.ControlyApplication;

import java.lang.reflect.Field;

/**
 * This class is used to override the default font of the application.
 */
public final class FontUtils {

    /**
     * Set the default font of the app.
     *
     * @param context The context of the current activity.
     */
    public static void setDefaultFont(Context context) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), ControlyApplication.DEFAULT_FONT);
        replaceFont(regular);
    }

    /**
     * This method uses reflection to override the default font type.
     *
     * @param newTypeface The new type face.
     */
    @SuppressWarnings("TryWithIdenticalCatches")
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

    public static void setTextViewFont(TextView titleView) {
        Typeface typeface = Typeface.createFromAsset(ControlyApplication.getInstance().getAssets(), ControlyApplication.DEFAULT_FONT);
        titleView.setTypeface(typeface);
    }
}
