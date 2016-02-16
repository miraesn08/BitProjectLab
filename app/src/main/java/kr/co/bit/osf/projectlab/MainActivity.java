package kr.co.bit.osf.projectlab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import kr.co.bit.osf.projectlab.db.CardDTO;
import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.db.StateDTO;

public class MainActivity extends AppCompatActivity {

    final String TAG = "FlashCardMainTag";

    FlashCardDB db = null;
    StateDTO cardState = null;
    List<CardDTO> cardList = null;

    ViewPager pager;

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
        // todo: find view pager
        pager = (ViewPager) findViewById(R.id.cardViewPager);
        // todo: set pager adapter
        pager.setAdapter(new CardViewPagerAdapter(getApplicationContext(), cardList));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // todo: write card state
        db.updateState(cardState);
        Log.i(TAG, "write card state:" + cardState);
    }

    // todo: pager adapter
    private class CardViewPagerAdapter extends PagerAdapter {
        List<CardDTO> list = null;

        private Context context = null;
        private LayoutInflater inflater;

        public CardViewPagerAdapter(Context context, List<CardDTO> list){
            super();
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
            Log.i(TAG, "list:size():" + list.size());
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i(TAG, "instantiateItem:position:" + position);
            View view = null;

            view = inflater.inflate(R.layout.activity_main_view_pager_child, null);
            ImageView img = (ImageView) view.findViewById(R.id.cardViewPagerChildImage);

            String imageName = list.get(position).getImagePath();
            int imageId = context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
            img.setImageResource(imageId);

            // todo: set click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childViewClicked(v);
                }
            });

            view.setTag(list.get(position));
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "destroyItem:position:" + position);
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void childViewClicked(View view) {
        CardDTO card = (CardDTO)view.getTag();
        Toast.makeText(getApplicationContext(), card.getName(), Toast.LENGTH_LONG).show();
        Log.i(TAG, "childViewClicked:card:" + card);
    }
}
