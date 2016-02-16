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
import android.widget.TextView;

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

            View view = inflater.inflate(R.layout.activity_main_view_pager_child, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.cardViewPagerChildImage);
            TextView textView = (TextView) view.findViewById(R.id.cardViewPagerChildText);

            PagerHolder holder = new PagerHolder(list.get(position), true, imageView, textView);
            // image
            String imageName = holder.card.getImagePath();
            int imageId = context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
            imageView.setImageResource(imageId);
            imageView.setVisibility(View.VISIBLE);
            // text
            textView.setText(holder.card.getName());
            textView.setVisibility(View.INVISIBLE);

            // todo: set click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childViewClicked(v);
                }
            });

            view.setTag(holder);
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
        PagerHolder holder = (PagerHolder)view.getTag();
        //Toast.makeText(getApplicationContext(), holder.card.getName(), Toast.LENGTH_LONG).show();

        // todo: flip animation by front/back state
        if (holder.isFront()) {
            // todo: show text
            (holder.getImageView()).setVisibility(View.INVISIBLE);
            (holder.getTextView()).setVisibility(View.VISIBLE);
        } else {
            // todo: show image
            (holder.getImageView()).setVisibility(View.VISIBLE);
            (holder.getTextView()).setVisibility(View.INVISIBLE);
        }

        // todo: change front/back state
        holder.flip();
        view.setTag(holder);
        Log.i(TAG, "childViewClicked:holder:" + holder);
    }

    private class PagerHolder {
        private CardDTO card;
        private boolean isFront;
        private ImageView imageView;
        private TextView textView;

        public PagerHolder(CardDTO card, boolean isFront, ImageView imageView, TextView textView) {
            this.isFront = isFront;
            this.imageView = imageView;
            this.textView = textView;
            this.card = card;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextView() {
            return textView;
        }

        public boolean isFront() {
            return isFront;
        }

        public void flip() {
            this.isFront = !this.isFront;
        }

        public CardDTO getCard() {
            return card;
        }

        @Override
        public String toString() {
            return "pagerHolder{" +
                    "isFront=" + isFront +
                    ", card=" + card +
                    '}';
        }
    }
}
