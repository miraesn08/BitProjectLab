package kr.co.bit.osf.projectlab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DemoViewActivity extends AppCompatActivity {
    final static String TAG = "DemoViewActivityLog";
    ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);

        flipper = (ViewFlipper) findViewById(R.id.demoViewFlipper);
        Log.i(TAG, "find view flipper");
        // add image
        int[] imageData = { R.drawable.dog, R.drawable.cat, R.drawable.rabbit };
        for (int imageId : imageData) {
            ImageView img = new ImageView(this);
            img.setImageResource(imageId);
            flipper.addView(img);
        }
        Log.i(TAG, "add image");

        //http://kitesoft.tistory.com/75
        //ViewFlipper가 View를 교체할 때 애니메이션이 적용되도록 설정
        //애니메이션은 안드로이드 시스템이 보유하고 있는  animation 리소스 파일 사용.
        //ViewFlipper의 View가 교체될 때 새로 보여지는 View의 등장 애니메이션
        //AnimationUtils 클래스 : 트윈(Tween) Animation 리소스 파일을 Animation 객체로 만들어 주는 클래스
        //AnimationUtils.loadAnimaion() - 트윈(Tween) Animation 리소스 파일을 Animation 객체로 만들어 주는 메소드
        //첫번째 파라미터 : Context
        //두번재 파라미터 : 트윈(Tween) Animation 리소스 파일(여기서는 안드로이드 시스템의 리소스 파일을 사용
        //                    (왼쪽에서 슬라이딩되며 등장)
        Animation showIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        //ViewFlipper에게 등장 애니메이션 적용
        flipper.setInAnimation(showIn);
        Log.i(TAG, "set in animation");

        //ViewFlipper의 View가 교체될 때 퇴장하는 View의 애니메이션
        //오른쪽으로 슬라이딩 되면 퇴장하는 애니메이션 리소스 파일 적용.
        //위와 다른 방법으로 애니메이션을 적용해봅니다.
        //첫번째 파라미터 : Context
        //두번재 파라미터 : 트윈(Tween) Animation 리소스 파일(오른쪽으로 슬라이딩되며 퇴장)
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);
        Log.i(TAG, "set out animation");

        // previous
        (findViewById(R.id.btnDemoViewPrevious)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showPrevious();
            }
        });
        // next
        (findViewById(R.id.btnDemoViewNext)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
            }
        });

        // state
        (findViewById(R.id.btnDemoViewGetFlipperState)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = "";
                state += "displayChild:" + flipper.getDisplayedChild();
                Toast.makeText(getApplicationContext(),state,Toast.LENGTH_SHORT).show();
                Log.i(TAG, "flipper state:" + state);
            }
        });
    }
}
