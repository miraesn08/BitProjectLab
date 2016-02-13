package kr.co.bit.osf.projectlab;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    final static String TAG = "CameraActivityLog";
    final int REQ_CODE_CAPTURE_THUMBNAIL = 101;
    final int REQ_CODE_CAPTURE_FULLSIZE = 102;
    final int REQ_CODE_SELECT_PICTURE = 103;
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 104;

    static final int MEDIA_TYPE_IMAGE = 1;
    static final int MEDIA_TYPE_VIDEO = 2;

    Button btnCaptureThumbnail = null;
    Button btnChoose = null;
    Button btnEnvironment = null;
    ImageView ivCaptured = null;
    private Uri fileUri;

    String mCurrentPhotoPath = null;
    int initialWidth = 0;
    int initialHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCaptureThumbnail = (Button) findViewById(R.id.btnCameraCaptureThumbnail);
        btnChoose = (Button) findViewById(R.id.btnChooseImage);
        btnEnvironment = (Button) findViewById(R.id.btnShowEnvironment);
        ivCaptured = (ImageView) findViewById(R.id.ivCameraCaptured);
            initialWidth = ivCaptured.getWidth();
            initialHeight = ivCaptured.getHeight();

/*
    http://developer.android.com/intl/ko/training/camera/photobasics.html
    Take a Photo with the Camera App
        The Android way of delegating actions to other applications is to invoke an Intent
        that describes what you want done. This process involves three pieces:
        The Intent itself, a call to start the external Activity, and some code to handle
        the image data when focus returns to your activity.
 */
        btnCaptureThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnCameraCapture clicked");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQ_CODE_CAPTURE_THUMBNAIL);
                    Log.i(TAG, "start cameraIntent");
                }
            }
        });

/*
    http://developer.android.com/intl/ko/training/camera/photobasics.html
    Save the Full-size Photo
        The Android Camera application saves a full-size photo if you give it a file to save into.
        You must provide a fully qualified file name where the camera app should save the photo.
        Generally, any photos that the user captures with the device camera should be saved
        on the device in the public external storage so they are accessible by all apps.
        The proper directory for shared photos is provided by getExternalStoragePublicDirectory(),
        with the DIRECTORY_PICTURES argument.
        Because the directory provided by this method is shared among all apps, reading and writing
        to it requires the READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions, respectively.
        The write permission implicitly allows reading, so if you need to write to the external storage
        then you need to request only one permission:

        Note: Files you save in the directories provided by getExternalFilesDir() are deleted
            when the user uninstalls your app.
*/
        (findViewById(R.id.btnCameraCaptureFullsize)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Log.i(TAG, "captured full-size image : path : " + photoFile.getAbsolutePath());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQ_CODE_CAPTURE_FULLSIZE);
                    }
                }
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnChooseImage clicked");
                // http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        REQ_CODE_SELECT_PICTURE);
            }
        });

        btnEnvironment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES):"
                        + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                Log.i(TAG, "Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES):"
                        + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            }
        });

        checkDangerousPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
/*
    http://developer.android.com/intl/ko/training/camera/photobasics.html

    Get the Thumbnail
        If the simple feat of taking a photo is not the culmination of your app's ambition,
        then you probably want to get the image back from the camera application and do something with it.
        The Android Camera application encodes the photo in the return Intent delivered to
        onActivityResult() as a small Bitmap in the extras, under the key "data".
        The following code retrieves this image and displays it in an ImageView.

        Note: This thumbnail image from "data" might be good for an icon, but not a lot more.
            Dealing with a full-sized image takes a bit more work.
*/
                case REQ_CODE_CAPTURE_THUMBNAIL:
                    Bundle extras = data.getExtras();
                    Bitmap thumbnail = (Bitmap) extras.get("data");
                    ivCaptured.setImageBitmap(thumbnail);
                    Log.i(TAG, "captured image");
                    break;

                case REQ_CODE_CAPTURE_FULLSIZE:
                    if (mCurrentPhotoPath != null) {
                        Log.i(TAG, "captured full-size image : path : " + mCurrentPhotoPath);
                        Log.i(TAG, "captured full-size image : setPic()");
                        setPic();
                        Log.i(TAG, "captured full-size image : galleryAddPic()");
                        galleryAddPic();
                        mCurrentPhotoPath = null;
                    }
                    Log.i(TAG, "captured full-size image");
                    break;

                case REQ_CODE_SELECT_PICTURE:
                    Uri selectedImageUri = data.getData();
                    String[] projection = { MediaStore.MediaColumns.DATA };
                    CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null, null);
                    Cursor cursor =cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    Log.i(TAG, "selectedImagePath=" + selectedImagePath);
                    Bitmap bm;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFile(selectedImagePath, options);
                    ivCaptured.setImageBitmap(bm);
                    //
                    Log.i(TAG, "selected image");
                    break;
                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                    Log.i(TAG, "CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE");
                    // Image captured and saved to fileUri specified in the Intent
                    Toast.makeText(this, "Image saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        Log.i(TAG, "captured full-size image : mCurrentPhotoPath : " + mCurrentPhotoPath);
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = initialWidth;         // ivCaptured.getWidth();
        int targetH = initialHeight;        // ivCaptured.getHeight();
        if (targetW == 0 || targetH == 0) {
            targetW = ivCaptured.getWidth();
            targetH = ivCaptured.getHeight();
        }
        Log.i(TAG, "captured full-size image : Get the size of the ImageView");
        Log.i(TAG, "captured full-size image : targetWidth : " + targetW);
        Log.i(TAG, "captured full-size image : targetHeight : " + targetH);

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        Log.i(TAG, "captured full-size image : Get the size of the image");
        Log.i(TAG, "captured full-size image : photoW : " + photoW);
        Log.i(TAG, "captured full-size image : photoH : " + photoH);

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }
        Log.i(TAG, "captured full-size image : scaleFactor : " + scaleFactor);

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Log.i(TAG, "captured full-size image : Set bitmap options to scale the image decode target");

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Log.i(TAG, "captured full-size image : Decode the JPEG file into a Bitmap");

		/* Associate the Bitmap to the ImageView */
        ivCaptured.setImageBitmap(bitmap);
        ivCaptured.setVisibility(View.VISIBLE);
        Log.i(TAG, "captured full-size image : Associate the Bitmap to the ImageView");
    }

    private void galleryAddPic() {
        Log.i(TAG, "captured full-size image : mCurrentPhotoPath : " + mCurrentPhotoPath);
        //
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Log.i(TAG, "captured full-size image : android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Log.i(TAG, "captured full-size image : new File(mCurrentPhotoPath)");
        Log.i(TAG, "captured full-size image : f : " + f.getAbsolutePath());
        Uri contentUri = Uri.fromFile(f);
        Log.i(TAG, "captured full-size image : contentUri : " + contentUri.getEncodedPath());
        mediaScanIntent.setData(contentUri);
        Log.i(TAG, "captured full-size image : mediaScanIntent.setData(contentUri)");
        this.sendBroadcast(mediaScanIntent);
        Log.i(TAG, "captured full-size image : this.sendBroadcast(mediaScanIntent);");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }
}
