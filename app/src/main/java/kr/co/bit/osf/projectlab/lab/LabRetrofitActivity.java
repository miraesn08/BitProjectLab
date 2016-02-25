package kr.co.bit.osf.projectlab.lab;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.bit.osf.projectlab.R;
import kr.co.bit.osf.projectlab.phonebook.PhoneBookListResult;
import kr.co.bit.osf.projectlab.phonebook.PhoneBookServiceClient;
import kr.co.bit.osf.projectlab.phonebook.PhoneBookServiceGenerator;
import kr.co.bit.osf.projectlab.phonebook.PhoneBookVO;
import retrofit2.Call;

public class LabRetrofitActivity extends AppCompatActivity {
    final String TAG = "LabRetrofitTag";
    Context context  = this;
    PhoneBookListResult phoneBookListResult;
    List<PhoneBookVO> phoneBookList;
    //
    ArrayList<String> list;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_retrofit);

        // https://futurestud.io/blog/retrofit-getting-started-and-android-client
        // Create a very simple REST adapter
        Log.i(TAG, "create service");
        PhoneBookServiceClient client = PhoneBookServiceGenerator.createService(PhoneBookServiceClient.class);

        // Fetch and print a list
        Log.i(TAG, "get list");
        final Call<PhoneBookListResult> call = client.getList();
        Log.i(TAG, "client.getList");
        // must use thread
        new Thread() {
            public void run() {
                try {
                    phoneBookListResult = call.execute().body();
                    Log.i(TAG, phoneBookListResult.toString());
                    refreshList();
                } catch (IOException e) {
                    // handle errors
                    Log.i(TAG, e.toString());
                }
            }
        }.start();

        final ListView listview = (ListView) findViewById(R.id.LabRetrofitListView);
        if (list == null) {
            list = new ArrayList<>();
        }
        for (int i = 0; i < 50; i++) {
            list.add("item " + (i + 1));
        }

        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list);
        Log.i(TAG, "set adapter");
        listview.setAdapter(adapter);
    }

    private void refreshList() {
        Log.i(TAG, "refreshList");
        if (phoneBookListResult != null) {
            phoneBookList = phoneBookListResult.getPhoneBookList();
            Log.i(TAG, "phoneBookList:size:" + phoneBookList.size());
            //
            list.clear();
            for(PhoneBookVO item : phoneBookList) {
                list.add(item.getName());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
