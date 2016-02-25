package kr.co.bit.osf.projectlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kr.co.bit.osf.projectlab.debug.Dlog;
import kr.co.bit.osf.projectlab.common.IntentExtrasName;
import kr.co.bit.osf.projectlab.common.IntentRequestCode;
import kr.co.bit.osf.projectlab.db.CardDTO;

public class CardEditActivity extends AppCompatActivity {
    private int intentRequestCode = 0;
    private int intentResultCode = RESULT_CANCELED;
    private CardDTO card = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        Dlog.i("card edit acvitty started");
        // todo: get intent data
        Intent intent = getIntent();
        intentRequestCode = intent.getIntExtra(IntentExtrasName.REQUEST_CODE, 0);;
        Dlog.i("intentRequestCode:" + intentRequestCode);
        switch (intentRequestCode) {
            case IntentRequestCode.CARD_EDIT:
                card = (CardDTO)(intent.getParcelableExtra(IntentExtrasName.SEND_DATA));
                break;
        }
        if (card != null) {
            Dlog.i("getExtras:card:" + card);
            card.setName("is updated");
            intentResultCode = RESULT_OK;
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(IntentExtrasName.RETURN_DATA, card);
        setResult(intentResultCode, data);
        Dlog.i("setResult:card:" + card);
        super.finish();
    }
}
