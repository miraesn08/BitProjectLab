package kr.co.bit.osf.projectlab.lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kr.co.bit.osf.projectlab.R;
import kr.co.bit.osf.projectlab.debug.Dlog;

public class LabStateChangeMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_state_change_main);
        Dlog.i("onCreate");

        (findViewById(R.id.labStateChangeMainRunButton))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabStateChangeMainActivity.this,
                        LabStateChangeSubActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dlog.i("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dlog.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dlog.i("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Dlog.i("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Dlog.i("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Dlog.i("onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Dlog.i("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Dlog.i("onRestoreInstanceState");
    }

}
