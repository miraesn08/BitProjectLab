package kr.co.bit.osf.projectlab.lab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import kr.co.bit.osf.projectlab.R;
import kr.co.bit.osf.projectlab.debug.Dlog;

public class LabStateChangeSubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_state_change_sub);
        Dlog.i("onCreate");

        (findViewById(R.id.labStateChangeSubInputTextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dlog.i("onClick");
                // http://twigstechtips.blogspot.kr/2011/10/android-allow-user-to-editinput-text.html
                final EditText nameTextView = new EditText(LabStateChangeSubActivity.this);
                Dlog.i("new EditText");
                nameTextView.setHint("your name, please!");
                Dlog.i("setHint");
                new AlertDialog.Builder(LabStateChangeSubActivity.this)
                        .setTitle("login")
                        .setMessage("input your name!")
                        .setView(nameTextView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((EditText)(findViewById(R.id.labStateChangeSubEditView)))
                                        .setText(nameTextView.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
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
    public void finish() {
        super.finish();
        Dlog.i("finish");
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
