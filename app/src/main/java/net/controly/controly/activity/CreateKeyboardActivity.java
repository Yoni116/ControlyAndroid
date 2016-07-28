package net.controly.controly.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.controly.controly.R;
import net.controly.controly.util.BitmapUtils;
import net.controly.controly.util.PermissionUtils;
import net.controly.controly.view.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This is the activity for creating a new keyboard.
 */
public class CreateKeyboardActivity extends BaseActivity {

    //-------Request codes-------
    private final int CAMERA_REQUEST_CODE = 0;
    private final int GALLERY_REQUEST_CODE = 1;

    ////-------Views-------
    private CircularImageView mKeyboardImage;
    private TextView mKeyboardName;

    private Button mPreviousbutton;
    private Button mNextButton;

    //Path for storing the image
    private String mImageFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_keyboard);

        final Context context = this;
        final String DEFAULT_KEYBOARD_IMAGE = "https://api.controly.net/ControlyApi/UserImages/defaultKeyboard.png"; //TODO This should be done offline.

        mKeyboardName = (TextView) findViewById(R.id.keyboard_name);

        mKeyboardImage = (CircularImageView) findViewById(R.id.keyboard_image);
        mKeyboardImage.setImageUrl(DEFAULT_KEYBOARD_IMAGE);
        mKeyboardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"Take photo", "Choose from library", "Change keyboard name", "Rotate", "Cancel"};

                new AlertDialog.Builder(context)
                        .setTitle("Add Keyboard Image")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {

                                    //Take a photo using the camera
                                    case 0:
                                        //TODO Make the text more user friendly
                                        if (!PermissionUtils.hasPermission(context, Manifest.permission.CAMERA)) {
                                            PermissionUtils.requestPermission(context, Manifest.permission.CAMERA, "We need a permission to access your camera so you can take a photo of your keyboard...");
                                        }

                                        //Get the intent for capturing an image with the camera
                                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                        //Ensure the the user gave permission to the camera, and that there's a camera activity for handling the intent.
                                        if (PermissionUtils.hasPermission(context, Manifest.permission.CAMERA) &&
                                                cameraIntent.resolveActivity(getPackageManager()) != null) {
                                            final String PACKAGE_NAME = getApplicationContext().getPackageName();

                                            //The time that the image was taken
                                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                                    .format(new Date());

                                            //Set the file path of the image to the controly folder
                                            mImageFilePath = Environment.getExternalStorageDirectory() +
                                                    "/Android/data/" + PACKAGE_NAME + "/Keyboard Images/Image-" +
                                                    timeStamp + ".png";

                                            File imageFile = new File(mImageFilePath);
                                            Uri imageFileUri = Uri.fromFile(imageFile);

                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                                        }

                                        break;

                                    //Choose a photo from the gallery
                                    case 1:
                                        //Set the intent for opening a photo with the gallery
                                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        galleryIntent.setType("image/*");

                                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                                        break;

                                    //Change the keyboard name
                                    case 2:
                                        final EditText input = new EditText(context);
                                        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                                        DialogInterface.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                mKeyboardName.setText(input.getText().toString());
                                            }
                                        };

                                        new AlertDialog.Builder(context)
                                                .setTitle("Set the keyboard name")
                                                .setView(input)
                                                .setPositiveButton("OK", onPositiveClick)
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                })
                                                .show();
                                        break;

                                    //Rotate the image
                                    case 3:
                                        mKeyboardImage.rotate(90);
                                }
                            }
                        }).show();
            }
        });

        //On previous button click, finish the activity.
        mPreviousbutton = (Button) findViewById(R.id.previous_button);
        mPreviousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = null;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                //Load the photo that the user just took
                case CAMERA_REQUEST_CODE:
                    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                    bmpFactoryOptions.inJustDecodeBounds = false;

                    photo = BitmapFactory.decodeFile(mImageFilePath, bmpFactoryOptions);

                    break;

                //Get the photo that the user selected
                case GALLERY_REQUEST_CODE:
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            //Preserve the photo dimensions and set the photo to the circular image view
            if (photo != null) {
                photo = BitmapUtils.preserveMaxDimensions(photo);
                mKeyboardImage.setImageBitmap(photo);
            }
        }
    }
}
