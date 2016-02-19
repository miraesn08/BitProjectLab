package kr.co.bit.osf.projectlab.lab;

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

import kr.co.bit.osf.projectlab.R;

public class LabViewPagerActivity extends AppCompatActivity {
    final String TAG = "LabViewPagerActivity";
    ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_view_pager);

        //Log.i(TAG, "onCreate");
        viewPager = (ViewPager) findViewById(R.id.labViewPagerViewPager);
        //Log.i(TAG, "viewPager");
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        //Log.i(TAG, "setAdapter");

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected:" + position);
                super.onPageSelected(position);
            }
        });
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private int[] list = new int[] {
                R.drawable.cat,
                R.drawable.dog,
                R.drawable.horse,
                R.drawable.rabbit
        };

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Log.i(TAG, "instantiateItem:" + position);
            LayoutInflater inflater = LayoutInflater.from(LabViewPagerActivity.this);
            View view = inflater.inflate(R.layout.activity_lab_view_pager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.labViewPagerItemImage);
            TextView textView = (TextView) view.findViewById(R.id.labViewPagerItemText);

            PagerHolder holder = new PagerHolder(position, list[position], true, imageView, textView);
            //Log.i(TAG, "instantiateItem:holder:" + holder);
            imageView.setImageResource(holder.getImageId());
            imageView.setVisibility(View.VISIBLE);
            textView.setText("position:" + holder.getPosition());
            textView.setVisibility(View.INVISIBLE);

            // set click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PagerHolder holder = (PagerHolder) v.getTag();
                    //Log.i(TAG, "onClick:holder:" + holder);

                    // switch
                    if (holder.isFront()) {
                        holder.getImageView().setVisibility(View.INVISIBLE);
                        holder.getTextView().setVisibility(View.VISIBLE);
                    } else {
                        holder.getImageView().setVisibility(View.VISIBLE);
                        holder.getTextView().setVisibility(View.INVISIBLE);
                    }
                    holder.flip();
                    //Log.i(TAG, "onClick:flip:" + holder);
                    Log.i(TAG, "onClick:currentItem:" + viewPager.getCurrentItem());

                    // save holder
                    v.setTag(holder);
                }
            });

            view.setTag(holder);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class PagerHolder {
        private int position;
        private int imageId;
        private boolean isFront;
        private ImageView imageView;
        private TextView textView;

        public PagerHolder(int position, int imageId, boolean isFront, ImageView imageView, TextView textView) {
            this.position = position;
            this.imageId = imageId;
            this.isFront = isFront;
            this.imageView = imageView;
            this.textView = textView;
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

        public int getImageId() {
            return imageId;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return "PagerHolder{" +
                    "position=" + position +
                    ", imageId=" + imageId +
                    ", isFront=" + isFront +
                    '}';
        }
    }

}
