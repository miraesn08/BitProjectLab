package kr.co.bit.osf.projectlab;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

public class ImageUtilActivity extends AppCompatActivity {
    static final String TAG = "ImageUtilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_util);

        (findViewById(R.id.btnGetOutputMediaFileUri)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnGetOutputMediaFileUri clicked");
                File outputName = ImageUtil.getOutputMediaFile(ImageUtil.MEDIA_TYPE_IMAGE);
                if (outputName != null) {
                    Log.i(TAG, outputName.getName());
                    Log.i(TAG, outputName.getAbsolutePath());
                } else {
                    Log.i(TAG, "no name!");
                }
            }
        });

        (findViewById(R.id.btnGetImageList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://stackoverflow.com/questions/4195660/get-list-of-photo-galleries-on-android
                // which image properties are we querying
                String[] projection = new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.DATA
                };

                // content:// style URI for the "primary" external storage volume
                Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Log.i(TAG, "images:" + images.getEncodedPath() );

                // Make the query.
                Cursor cur = getContentResolver().query(images,
                        projection, // Which columns to return
                        null,       // Which rows to return (all rows)
                        null,       // Selection arguments (none)
                        null        // Ordering
                );
                if (cur == null) {
                    Log.i(TAG, "cursor is null");
                    return;
                }

                Log.i(TAG, "query count=" + cur.getCount());

                if (cur.moveToFirst()) {
                    String bucket;
                    String date;
                    String data;

                    int bucketColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                    int dateColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATE_TAKEN);

                    int dataColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATA);

                    do {
                        // Get the field values
                        bucket = cur.getString(bucketColumn);
                        date = cur.getString(dateColumn);
                        data = cur.getString(dataColumn);

                        // Do something with the values.
                        Log.i(TAG, " bucket=" + bucket
                                + "  date_taken=" + date
                                + "  data=" + data);
                    } while (cur.moveToNext());
                }
            }
        });
    }
}
