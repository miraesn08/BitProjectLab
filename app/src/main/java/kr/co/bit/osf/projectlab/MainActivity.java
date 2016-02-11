package kr.co.bit.osf.projectlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    final String TAG = "projectLabTag";
    Button btnCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = (Button)findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnCamera clicked");
                Intent cameraLabIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraLabIntent);

                // http://www.tutorialspoint.com/android/android_camera.htm
                // Using existing android camera application in our application
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            }
        });
    }

}
