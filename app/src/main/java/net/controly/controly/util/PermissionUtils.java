package net.controly.controly.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import net.controly.controly.R;

/**
 * This class contains an assortment of util methods relating to user permissions.
 */
public class PermissionUtils {

    public static final int GRANT_PERMISSION_REQUEST_CODE = 0;

    /**
     * This method shows the UI that asks a certain permission from the user only if he didn't grant it already.
     *
     * @param context    The context of the application.
     * @param permission The permission to request.
     * @param dialogText The text in the dialog explaining the reason for the permission.
     */
    public static void requestPermission(final Context context, final String permission, final String dialogText) {

        Logger.info("Requesting " + permission + " permission.");

        //Check if we already have the permission.
        if (!hasPermissions(context, permission)) {

            if (dialogText.length() > 0) {
                //Show a dialog explaining why we need this permission.
                final String dialogTitle = "We need a permission";

                new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                        .setTitle(dialogTitle)
                        .setMessage(dialogText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Request the permission from the user.
                                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, GRANT_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, GRANT_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * This method returns whether we have the given permissions.
     *
     * @param context     The context of the application.
     * @param permissions The permissions to check.
     * @return Whether we have the given permission.
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return Whether the permission was granted
     */
    public static boolean permissionGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

}
