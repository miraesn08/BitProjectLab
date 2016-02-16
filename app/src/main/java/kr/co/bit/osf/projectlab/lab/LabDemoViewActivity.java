package kr.co.bit.osf.projectlab.lab;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bit.osf.projectlab.R;
import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.db.CardDTO;

public class LabDemoViewActivity extends AppCompatActivity {
    final static String TAG = "DemoViewActivityLog";
    ViewPager pager;

    // card data list
    CardDTO[] cardList = {
            new CardDTO("dog","dog", FlashCardDB.CardEntry.TYPE_DEMO, 1),
            new CardDTO("cat", "cat", FlashCardDB.CardEntry.TYPE_DEMO, 1),
            new CardDTO("rabbit", "rabbit", FlashCardDB.CardEntry.TYPE_DEMO, 1)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_demo_view);

        // http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndorid-Viewpager-%EC%82%AC%EC%9A%A9-%ED%95%98%EA%B8%B0
        pager = (ViewPager) findViewById(R.id.demoViewPager);
        pager.setAdapter(new DemoViewPagerAdapter(getApplicationContext(),
                new ArrayList<>(Arrays.asList(cardList))));
    }

    /**
     * PagerAdapter
     * http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndorid-Viewpager-%EC%82%AC%EC%9A%A9-%ED%95%98%EA%B8%B0
     */
    private class DemoViewPagerAdapter extends PagerAdapter {
        List<CardDTO> list = null;

        private Context context = null;
        private LayoutInflater inflater;

        public DemoViewPagerAdapter(Context context, List<CardDTO> list){
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

            //http://kitesoft.tistory.com/76
            //새로운 View 객체를 Layoutinflater를 이용해서 생성
            //만들어질 View의 설계는 res폴더>>layout폴더>>viewpater_childview.xml 레이아웃 파일 사용
            view = inflater.inflate(R.layout.activity_lab_demo_view_pager_child, null);

            //만들어진 View안에 있는 ImageView 객체 참조
            //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
            ImageView img = (ImageView) view.findViewById(R.id.imageViewPagerChildImage);

            //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
            //현재 position에 해당하는 이미지를 setting
            String imageName = list.get(position).getImagePath();
            // http://stackoverflow.com/questions/6783327/setimageresource-from-a-string
            int imageId = context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
            img.setImageResource(imageId);

            // click listener
            view.setTag(list.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childViewClicked(v);
                }
            });

            //ViewPager에 만들어 낸 View 추가
            container.addView(view);

            //Image가 세팅된 View를 리턴
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "destroyItem:position:" + position);
            //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
            //첫번째 파라미터 : ViewPager
            //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
            //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void childViewClicked(View v) {
        CardDTO card = (CardDTO)v.getTag();
        Toast.makeText(getApplicationContext(), card.getName(), Toast.LENGTH_LONG).show();
        Log.i(TAG, "childViewClicked:card:" + card);
    }
}
