package kr.co.bit.osf.projectlab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kr.co.bit.osf.projectlab.common.ImageUtil;
import kr.co.bit.osf.projectlab.common.IntentRequestCode;
import kr.co.bit.osf.projectlab.db.CardDAO;
import kr.co.bit.osf.projectlab.db.CardDTO;
import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.db.StateDAO;
import kr.co.bit.osf.projectlab.db.StateDTO;
import kr.co.bit.osf.projectlab.flip3d.DisplayNextView;
import kr.co.bit.osf.projectlab.flip3d.Flip3dAnimation;

public class MainActivity extends AppCompatActivity {

    final String TAG = "FlashCardMainTag";

    FlashCardDB db = null;
    StateDAO stateDao = null;
    StateDTO cardState = null;
    CardDAO cardDao = null;
    List<CardDTO> cardList = null;

    ViewPager pager;
    CardViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // full screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        // read state from db
        db = new FlashCardDB(this);
        stateDao = db;
        cardState = stateDao.getState();
        if (cardState == null) {
            Log.i(TAG, "db initialize:");
            db.initialize();
            cardState = stateDao.getState();
        }
        Log.i(TAG, "read card state:" + cardState);

        // read card list by state
        cardDao = db;
        cardList = cardDao.getCardByBoxId(cardState.getBoxId());
        Log.i(TAG, "card list:size:" + cardList.size());
        Log.i(TAG, "card list:value:" + cardList);

        // show card list
        // find view pager
        pager = (ViewPager) findViewById(R.id.cardViewPager);
        // set pager adapter
        pagerAdapter = new CardViewPagerAdapter(getApplicationContext(), cardList);
        pager.setAdapter(pagerAdapter);

        // add card from gallery
        (findViewById(R.id.cardViewGalleryButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "cardViewGalleryButton clicked");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, getString(R.string.title_intent_select_picture)),
                        IntentRequestCode.SELECT_PICTURE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // write card state
        stateDao.updateState(cardState);
        Log.i(TAG, "write card state:" + cardState);
    }

    // pager adapter
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

            // set click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childViewClicked(v);
                }
            });

            // set long click listener
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    childViewLongClicked(v);
                    return true;
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

        // flip animation by front/back state
        if (holder.isFront()) {
            // show text
            applyRotation(holder.isFront(), 0, -90, holder.getImageView(), holder.getTextView());
        } else {
            // show image
            applyRotation(holder.isFront(), 0, 90, holder.getImageView(), holder.getTextView());
        }
        // change front/back state
        holder.flip();

        // save holder
        view.setTag(holder);
        Log.i(TAG, "childViewClicked:holder:" + holder);
    }

    private void childViewLongClicked(View view) {
        PagerHolder holder = (PagerHolder)view.getTag();
        holder.card.setName("is updated");
        Toast.makeText(getApplicationContext(), holder.card.getName(), Toast.LENGTH_SHORT).show();
        view.setTag(holder);
        pagerAdapter.notifyDataSetChanged();    // update view pager
        Log.i(TAG, "childViewLongClicked");
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
                case IntentRequestCode.SELECT_PICTURE:
                    // get image path
                    String imagePath = ImageUtil.getImagePathFromIntentData(this, data);
                    Log.i(TAG,"selected picture path:" + imagePath);
                    // get image name
                    String imageName = ImageUtil.getNameFromPath(imagePath);
                    Log.i(TAG,"selected picture name:" + imageName);
                    // get card dto
                    CardDTO newCard =  new CardDTO(imageName, imagePath,
                            FlashCardDB.CardEntry.TYPE_USER, cardState.getBoxId());
                    // get last sequence and set next sequence
                    Log.i(TAG, "get last sequence:" + cardList.get(cardList.size() - 1).getSeq());
                    newCard.setSeq(cardList.get(cardList.size() - 1).getSeq() + 1);
                    // save new card to db
                    cardDao.addCard(newCard);
                    // add card list
                    cardList.add(newCard);
                    pagerAdapter.notifyDataSetChanged();    // update view pager
                    Log.i(TAG, "add card list:" + imageName);
                    break;
                case IntentRequestCode.CAPTURE_IMAGE:
                    break;
            }
        }
    }

    // http://www.inter-fuser.com/2009/08/android-animations-3d-flip.html
    private void applyRotation(boolean isFirstImage, float start, float end,
                               ImageView imageView, TextView textView) {
        // Find the center of image
        final float centerX = imageView.getWidth() / 2.0f;
        final float centerY = imageView.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(isFirstImage, imageView, textView));

        if (isFirstImage) {
            imageView.startAnimation(rotation);
        } else {
            textView.startAnimation(rotation);
        }
    }
}
