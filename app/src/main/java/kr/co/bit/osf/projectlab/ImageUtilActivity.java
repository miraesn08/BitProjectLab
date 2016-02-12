package kr.co.bit.osf.projectlab;

import android.os.Bundle;
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
                Log.i(TAG,"btnGetOutputMediaFileUri clicked");
                File outputName = ImageUtil.getOutputMediaFile(ImageUtil.MEDIA_TYPE_IMAGE);
                if (outputName != null) {
                    Log.i(TAG, outputName.getName());
                    Log.i(TAG, outputName.getAbsolutePath());
                } else {
                    Log.i(TAG, "no name!");
                }
            }
        });
    }
}
