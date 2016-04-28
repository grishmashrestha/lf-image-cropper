package com.lftechnology.imagecropper;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lftechnology.imagecropper.helper.ImageUploadHelper;
import com.soundcloud.android.crop.Crop;

public class MainActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 10;
    public static final int CHOOSE_PHOTO = 11;

    private ImageView imageView;
    private Uri inputUri, outputUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.ivImage);
    }

    public void selectImage(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // start the image capture Intent
                    startActivityForResult(intent, TAKE_PHOTO);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),CHOOSE_PHOTO);
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO && data != null) {
            inputUri = data.getData();
            performCrop();
        } else if (requestCode == TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                inputUri = data.getData();
                performCrop();
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, "Aha!", Toast.LENGTH_SHORT).show();
            imageView.setImageURI(outputUri);
        }
    }

    private void performCrop() {
        outputUri = ImageUploadHelper.getOutputMediaFileUri(ImageUploadHelper.MEDIA_TYPE_IMAGE);
        Crop.of(inputUri, outputUri).asSquare().start(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
