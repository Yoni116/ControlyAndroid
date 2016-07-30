package net.controly.controly.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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

    public static String getViewId(View view) {
        return view.getResources().getResourceName(view.getId());
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
}
