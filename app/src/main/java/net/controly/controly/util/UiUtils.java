package net.controly.controly.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * This class contains an assortment of user interface util methods.
 */
public class UIUtils {

    /**
     * Starts the given activity.
     *
     * @param activityClass The activity to start.
     */
    public static void startActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);

        if (Build.VERSION.SDK_INT >= 11) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        context.startActivity(intent);
    }

    /**
     * Get a given view's resource id.
     *
     * @param view The given view.
     * @return String resource id of the given view.
     */
    private static String getViewId(View view) {
        return view.getResources().getResourceName(view.getId());
    }

    /**
     * Add a given view to a given layout.
     *
     * @param layout The layout to add the view to.
     * @param view   The view to add to the layout.
     * @param x      The x location of the new view - in px.
     * @param y      The y location of the new view - in px.
     * @param width  The width of the new view.
     * @param height The height of the new view.
     */
    public static void drawView(ViewGroup layout, View view, float x, float y, int width, int height) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);

        view.setX(x);
        view.setY(y);

        layout.addView(view, params);
    }

    /**
     * This method moves a view to the given location.
     *
     * @param view The view to move.
     * @param x    The x coordinate of the new location.
     * @param y    The y coordinate of the new location.
     */
    public static void moveView(View view, float x, float y) {
        view.setX(x);
        view.setY(y);
    }

    /**
     * Setup email auto complete for the given auto complete text view.
     *
     * @param context The context of the application.
     * @param view    The view to set the auto complete for.
     */
    public static void setupEmailAutoComplete(Context context, AutoCompleteTextView view) {
        Logger.info("Setting up email auto complete for " + getViewId(view));

        final Uri emailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        Cursor emailCur = cr.query(emailContentUri, null, null, null, null);
        ArrayList<String> emailsCollection = new ArrayList<>();

        //Avoid NullPointerException
        if (emailCur == null) {
            return;
        }

        while (emailCur.moveToNext()) {
            final int columnIndex = emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            String email = emailCur.getString(columnIndex);

            emailsCollection.add(email);
        }

        emailCur.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, emailsCollection);
        view.setAdapter(adapter);
    }

    /**
     * Sets the text color of the given {@link SearchView} to white.
     *
     * @param context    The context of the application.
     * @param searchView The search view to set the text color for.
     */
    public static void setSearchBarTextColor(Context context, SearchView searchView) {
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchEditText.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(context, android.R.color.white));
        searchEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    /**
     * Change the icon color of the given {@link FloatingActionButton}
     *
     * @param fab   The FAB to change the icon color of.
     * @param color The color to change to.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void changeFabIconColor(FloatingActionButton fab, int color) {
        Drawable newDrawable = fab.getDrawable()
                .getConstantState()
                .newDrawable()
                .mutate();

        newDrawable.setTint(color);
        fab.setImageDrawable(newDrawable);
    }

    /**
     * Gets the on click listener of a give view.
     *
     * @param view The view.
     * @return The view's OnClickListener.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static View.OnClickListener getOnClickListener(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        String lInfoStr = "android.view.View$ListenerInfo";

        try {
            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (NoSuchFieldException ex) {
            Logger.error("Reflection: No Such Field.");
        } catch (IllegalAccessException ex) {
            Logger.error("Reflection: Illegal Access.");
        } catch (ClassNotFoundException ex) {
            Logger.error("Reflection: Class Not Found.");
        }

        return retrievedListener;
    }
}
