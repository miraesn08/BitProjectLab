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
import android.widget.EditText;

import kr.co.bit.osf.projectlab.R;
import kr.co.bit.osf.projectlab.common.IntentRequestCode;
import kr.co.bit.osf.projectlab.debug.Dlog;

public class LabStateChangeMainActivity extends AppCompatActivity {
    // http://www.informit.com/articles/article.aspx?p=2262133&seqNum=4
    OrientationEventListener mOrientationListener;

    // orientation
    final String stateDataName = "StateData";
    ActivityState currentState;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_state_change_main);
        Dlog.i("onCreate");

        editText = (EditText) findViewById(R.id.labStateChangeMainEditText);

        (findViewById(R.id.labStateChangeMainRunButton))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dlog.i("labStateChangeMainRunButton:clicked:");
                        Intent intent = new Intent(LabStateChangeMainActivity.this,
                                LabStateChangeSubActivity.class);
                        startActivityForResult(intent, IntentRequestCode.CARD_EDIT);
                        Dlog.i("labStateChangeMainRunButton:clicked:startActivityForResult");
                    }
                });

        // orientation
        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                currentState.changed();
                editText.setText(currentState.toString());
                Dlog.i(currentState.toString());
            }
        };

        if (mOrientationListener.canDetectOrientation()) {
            Dlog.v("Can detect orientation");
            mOrientationListener.enable();
        } else {
            Dlog.v("Cannot detect orientation");
            mOrientationListener.disable();
        }

        // is first time ?
        try {
            currentState = savedInstanceState.getParcelable(stateDataName);
            Dlog.i("is not first time");
        } catch (Exception e) {
            Dlog.i("is first time");
            currentState = new ActivityState();
        }
        editText.setText(currentState.toString());
        Dlog.i(currentState.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Dlog.i("onActivityResult:requestCode:" + requestCode);
        Dlog.i("onActivityResult:resultCode:" + resultCode);
        Dlog.i("onActivityResult:Intent data is null:" + (data == null));
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
        outState.putParcelable(stateDataName, currentState);
        Dlog.i("onSaveInstanceState:" + currentState);
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

    private class ActivityState implements Parcelable {
        int orientation;
        int count;

        public ActivityState() {
            this.orientation = getResources().getConfiguration().orientation;
            this.count = 0;
        }

        public ActivityState(int orientation) {
            this.orientation = orientation;
            this.count = 0;
        }

        protected ActivityState(Parcel in) {
            orientation = in.readInt();
            count = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(orientation);
            dest.writeInt(count);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<ActivityState> CREATOR = new Creator<ActivityState>() {
            @Override
            public ActivityState createFromParcel(Parcel in) {
                return new ActivityState(in);
            }

            @Override
            public ActivityState[] newArray(int size) {
                return new ActivityState[size];
            }
        };

        public int getOrientation() {
            return orientation;
        }

        public void changed() {
            setOrientation(getResources().getConfiguration().orientation);
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
            this.count++;
        }

        @Override
        public String toString() {
            return "ActivityState{" +
                    "orientation=" + orientation +
                    "," + ((orientation == Configuration.ORIENTATION_PORTRAIT)
                            ? "portrait" : "landscape") +
                    ",count=" + count +
                    '}';
        }
    }
}
