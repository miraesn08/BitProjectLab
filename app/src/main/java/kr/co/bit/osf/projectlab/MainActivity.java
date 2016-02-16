package kr.co.bit.osf.projectlab;

import android.content.Context;
import android.content.Intent;
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

import kr.co.bit.osf.projectlab.common.ImageUtil;
import kr.co.bit.osf.projectlab.db.CardDAO;
import kr.co.bit.osf.projectlab.db.CardDTO;
import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.db.StateDAO;
import kr.co.bit.osf.projectlab.db.StateDTO;

public class MainActivity extends AppCompatActivity {

    final String TAG = "FlashCardMainTag";

    FlashCardDB db = null;
    StateDAO stateDao = null;
    StateDTO cardState = null;
    CardDAO cardDao = null;
    List<CardDTO> cardList = null;

    ViewPager pager;
    CardViewPagerAdapter pagerAdapter;

    final int REQ_CODE_SELECT_PICTURE = 501;
    final int REQ_CODE_CAPTURE_IMAGE = 502;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // todo: read state from db
        db = new FlashCardDB(this);
        stateDao = db;
        cardState = stateDao.getState();
        if (cardState == null) {
            Log.i(TAG, "db initialize:");
            db.initialize();
            cardState = stateDao.getState();
        }
        Log.i(TAG, "read card state:" + cardState);

        // todo: read card list by state
        cardDao = db;
        cardList = cardDao.getCardByBoxId(cardState.getBoxId());
        Log.i(TAG, "card list:size:" + cardList.size());
        Log.i(TAG, "card list:value:" + cardList);

        // todo: show card list
        // todo: find view pager
        pager = (ViewPager) findViewById(R.id.cardViewPager);
        // todo: set pager adapter
        pagerAdapter = new CardViewPagerAdapter(getApplicationContext(), cardList);
        pager.setAdapter(pagerAdapter);

        // todo: add card from gallery
        (findViewById(R.id.cardViewGalleryButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "cardViewGalleryButton clicked");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        REQ_CODE_SELECT_PICTURE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // todo: write card state
        stateDao.updateState(cardState);
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
            String imagePath = holder.card.getImagePath();
            if (holder.card.getType() == FlashCardDB.CardEntry.TYPE_DEMO) {
                // card demo data
                int imageId = context.getResources().getIdentifier("drawable/" + imagePath, null, context.getPackageName());
                imageView.setImageResource(imageId);
            } else {
                // load image from sd card
                ImageUtil.showImageFileInImageView(imagePath, imageView);
            }
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

        @Override
        public int getItemPosition(Object object) {
            // http://stackoverflow.com/questions/10611018/how-to-update-viewpager-content
            // update ViewPager content, but not so good
            return POSITION_NONE;
        }
    }

    private void childViewClicked(View view) {
        PagerHolder holder = (PagerHolder)view.getTag();
        //Toast.makeText(getApplicationContext(), holder.card.getName(), Toast.LENGTH_LONG).show();

        // todo: change front/back state
        holder.flip();

        // todo: flip animation by front/back state
        if (holder.isFront()) {
            // todo: show image
            (holder.getImageView()).setVisibility(View.VISIBLE);
            (holder.getTextView()).setVisibility(View.INVISIBLE);
        } else {
            // todo: show text
            (holder.getImageView()).setVisibility(View.INVISIBLE);
            (holder.getTextView()).setVisibility(View.VISIBLE);
        }

        // todo: save holder
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_SELECT_PICTURE:
                    // todo: get image path
                    String imagePath = ImageUtil.getImagePathFromIntentData(this, data);
                    Log.i(TAG,"selected picture path:" + imagePath);
                    // todo: get image name
                    String imageName = ImageUtil.getNameFromPath(imagePath);
                    Log.i(TAG,"selected picture name:" + imageName);
                    // todo: get card dto
                    CardDTO newCard =  new CardDTO(imageName, imagePath,
                            FlashCardDB.CardEntry.TYPE_USER, cardState.getBoxId());
                    // todo: get last sequence and set next sequence
                    Log.i(TAG, "get last sequence:" + cardList.get(cardList.size() - 1).getSeq());
                    newCard.setSeq(cardList.get(cardList.size() - 1).getSeq() + 1);
                    // todo: save new card to db
                    cardDao.addCard(newCard);
                    // todo: add card list
                    cardList.add(newCard);
                    pagerAdapter.notifyDataSetChanged();    // update view pager
                    Log.i(TAG, "add card list:" + imageName);
                    break;
                case REQ_CODE_CAPTURE_IMAGE:
                    break;
            }
        }
    }
}
