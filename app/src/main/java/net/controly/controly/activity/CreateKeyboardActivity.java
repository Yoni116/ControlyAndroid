package net.controly.controly.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import net.controly.controly.R;
import net.controly.controly.util.PermissionUtils;
import net.controly.controly.view.CircularNetworkImageView;

/**
 * Created by Itai on 27-Jul-16.
 */
public class CreateKeyboardActivity extends BaseActivity {

    private CircularNetworkImageView mKeyboardImage;
    private final int CAMERA_REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_keyboard);

        final Context context = this;

        mKeyboardImage = (CircularNetworkImageView) findViewById(R.id.keyboard_image);
        mKeyboardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"Take Photo", "Choose from library", "Cancel"};

                new AlertDialog.Builder(context)
                        .setTitle("Add Keyboard Image")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:

                                        //TODO Make the text more user friendly
                                        if (!PermissionUtils.hasPermission(context, Manifest.permission.CAMERA)) {
                                            PermissionUtils.requestPermission(context, Manifest.permission.CAMERA, "We need a permission to access your camera so you can take a photo of your keyboard...");
                                        }

                                        if (PermissionUtils.hasPermission(context, Manifest.permission.CAMERA)) {
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                                        }

                                        break;
                                    case 1:
                                        break;

                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mKeyboardImage.setImage(photo);
            }
        }
    }
}
