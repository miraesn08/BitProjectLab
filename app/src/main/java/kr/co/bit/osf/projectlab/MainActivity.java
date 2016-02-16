package kr.co.bit.osf.projectlab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import kr.co.bit.osf.projectlab.db.CardDTO;
import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.db.StateDTO;

public class MainActivity extends AppCompatActivity {

    final String TAG = "FlashCardMainTag";

    FlashCardDB db = null;
    StateDTO cardState = null;
    List<CardDTO> cardList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // todo: read state from db
        db = new FlashCardDB(this);
        cardState = db.getState();
        if (cardState == null) {
            Log.i(TAG, "db initialize:");
            db.initialize();
            cardState = db.getState();
        }
        Log.i(TAG, "read card state:" + cardState);

        // todo: read card list by state
        cardList = db.getCardByBoxId(cardState.getBoxId());
        Log.i(TAG, "card list:size:" + cardList.size());
        Log.i(TAG, "card list:value:" + cardList);

        // todo: show card list

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // todo: write card state
        db.updateState(cardState);
        Log.i(TAG, "write card state:" + cardState);
    }
}
