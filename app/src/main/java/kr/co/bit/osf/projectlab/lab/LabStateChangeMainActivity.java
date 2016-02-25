package kr.co.bit.osf.projectlab.lab;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Toast;

import kr.co.bit.osf.projectlab.R;
import kr.co.bit.osf.projectlab.common.IntentRequestCode;
import kr.co.bit.osf.projectlab.debug.Dlog;

public class LabStateChangeMainActivity extends AppCompatActivity {
    // http://www.informit.com/articles/article.aspx?p=2262133&seqNum=4
    OrientationEventListener mOrientationListener;

    // orientation state
    final String stateData = "stateData";
    OrientationState state;

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
                        startActivityForResult(intent, IntentRequestCode.CARD_EDIT);
                    }
                });

        // orienation
        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                Dlog.v("Orientation changed to " + orientation);

            }
        };

        if (mOrientationListener.canDetectOrientation()) {
            Dlog.v("Can detect orientation");
            mOrientationListener.enable();
        } else {
            Dlog.v("Cannot detect orientation");
            mOrientationListener.disable();
        }

        // orientation state
        if (state == null) {
            Dlog.i("state == null");
            // http://stackoverflow.com/questions/10380989/how-do-i-get-the-current-orientation-activityinfo-screen-orientation-of-an-a
            state = new OrientationState(getResources().getConfiguration().orientation);
            Dlog.i("getResources().getConfiguration().orientation:" + state);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Dlog.i("onActivityResult:requestCode:" + requestCode);
        Dlog.i("onActivityResult:resultCode:" + resultCode);
        Dlog.i("onActivityResult:Intent data is null:" + (data == null));
    }

    // http://stackoverflow.com/questions/5726657/how-to-detect-orientation-change-in-layout-in-android
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Dlog.i("onConfigurationChanged");

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dlog.i("onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Dlog.i("onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dlog.i("onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(stateData, state);
        Dlog.i("onSaveInstanceState:state:" + state);
    }

    @Override
    public void finish() {
        Dlog.i("finish");
        super.finish();
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

    private class OrientationState implements Parcelable {
        int orientation;

        public OrientationState(int orientation) {
            this.orientation = orientation;
        }

        protected OrientationState(Parcel in) {
            orientation = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(orientation);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<OrientationState> CREATOR = new Creator<OrientationState>() {
            @Override
            public OrientationState createFromParcel(Parcel in) {
                return new OrientationState(in);
            }

            @Override
            public OrientationState[] newArray(int size) {
                return new OrientationState[size];
            }
        };

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        @Override
        public String toString() {
            return "OrientationState{" +
                    "orientation=" + orientation +
                    "," + ((orientation == Configuration.ORIENTATION_PORTRAIT)
                            ? "portrait" : "landscape") +
                    '}';
        }
    }
}
