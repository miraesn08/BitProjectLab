package kr.co.bit.osf.projectlab.lab;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.bit.osf.projectlab.R;

public class LabRetrofitActivity extends AppCompatActivity {
    final String TAG = "LabRetrofitTag";
    Context context  = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_retrofit);

        final ListView listview = (ListView) findViewById(R.id.LabRetrofitListView);
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"};
        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("item " + (i + 1));
        }
        for (String item : values) {
            list.add(item);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(context,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            Log.i(TAG, "getItemId:" + position);
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
