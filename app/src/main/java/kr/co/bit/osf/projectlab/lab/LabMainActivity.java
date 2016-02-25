package kr.co.bit.osf.projectlab.lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import kr.co.bit.osf.projectlab.R;

public class LabMainActivity extends AppCompatActivity {

    final String TAG = "LabMainTag";
    Button btnCamera = null;
    Button btnImageUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_main);

        btnCamera = (Button) findViewById(R.id.btnLabCamera);
        btnImageUtil = (Button) findViewById(R.id.btnLabImageUtil);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnCamera clicked");
                Intent cameraLabIntent = new Intent(getApplicationContext(), LabCameraActivity.class);
                startActivity(cameraLabIntent);
            }
        });

        btnImageUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnImageUtil clicked");
                Intent intent = new Intent(getApplicationContext(), LabImageUtilActivity.class);
                startActivity(intent);
            }
        });

        (findViewById(R.id.btnLabListView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnListView clicked");
                Intent intent = new Intent(getApplicationContext(), LabListViewActivity.class);
                startActivity(intent);
            }
        });

        (findViewById(R.id.btnLabDemoView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnDemoView clicked");
                Intent intent = new Intent(getApplicationContext(), LabDemoViewActivity.class);
                startActivity(intent);
            }
        });
    }

}
