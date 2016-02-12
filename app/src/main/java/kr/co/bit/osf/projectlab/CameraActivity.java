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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    final static String TAG = "projectLabTag";
    final int REQ_CODE_CAPTURE = 101;
    final int REQ_CODE_SELECT_PICTURE = 102;
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 103;

    static final int MEDIA_TYPE_IMAGE = 1;
    static final int MEDIA_TYPE_VIDEO = 2;

    Button btnCapture = null;
    Button btnCapture2 = null;
    Button btnChoose = null;
    Button btnEnvironment = null;
    ImageView ivCaptured = null;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCapture = (Button) findViewById(R.id.btnCameraCapture);
        btnCapture2 = (Button) findViewById(R.id.btnCameraCapture2);
        btnChoose = (Button) findViewById(R.id.btnChooseImage);
        btnEnvironment = (Button) findViewById(R.id.btnShowEnvironment);
        ivCaptured = (ImageView) findViewById(R.id.ivCameraCaptured);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnCameraCapture clicked");
                // http://www.tutorialspoint.com/android/android_camera.htm
                // Using existing android camera application in our application
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQ_CODE_CAPTURE);
                Log.i(TAG, "start cameraIntent");
            }
        });

        btnCapture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnCameraCapture2 clicked");
                // http://developer.android.com/intl/ko/guide/topics/media/camera.html
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.i(TAG, "new camera2Intent");

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                Log.i(TAG, "fileUri:" + fileUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                Log.i(TAG, "start camera2Intent");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_CAPTURE:
                    // http://www.tutorialspoint.com/android/android_camera.htm
                    //Bitmap bp = (Bitmap) data.getExtras().get("data");
                    //ivCaptured.setImageBitmap(bp);
                    // Handling Image
                    // http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String fileName = System.currentTimeMillis() + ".jpg";
                    // Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 800, 150, true);
                    //File destination = new File(Environment.getExternalStorageDirectory(), fileName);
                    //Log.i(TAG, "getExternalStorageDirectory=" + Environment.getExternalStorageDirectory());
                    File destination = new File(getExternalFilesDir(null), fileName);
                    //String fileName="/dcim/camera/"+ System.currentTimeMillis() + ".jpg";
                    //File destination = new File(Environment.getExternalStorageDirectory(), fileName);
                    Log.i(TAG, "image destination=" + destination);
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ivCaptured.setImageBitmap(thumbnail);
                    //
                    Log.i(TAG, "captured image");
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

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        Log.i(TAG, "getOutputMediaFileUri(" + type + ")");
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        Log.i(TAG, "getOutputMediaFile(" + type + ")");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "");
        Log.i(TAG, "mediaStorageDir:" + mediaStorageDir);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.i(TAG, "failed to create directory:" + mediaStorageDir);
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
