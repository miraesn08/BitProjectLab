package kr.co.bit.osf.projectlab.lab;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.bit.osf.projectlab.R;

// http://stackoverflow.com/questions/7785649/creating-a-3d-flip-animation-in-android-using-xml
public class LabFlipAnimationActivity extends AppCompatActivity {
    ValueAnimator mFlipAnimator;
    ImageView frontView;
    TextView backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_flip_animation);

        frontView = (ImageView)findViewById(R.id.flipAnimationFrontView);
        backView = (TextView)findViewById(R.id.flipAnimationBackView);

        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        mFlipAnimator.addUpdateListener(new FlipListener(frontView, backView));

        (findViewById(R.id.flipAnimationFlipButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlip();
            }
        });
    }

    private void toggleFlip() {
        if(isFlipped()){
            mFlipAnimator.reverse();
        } else {
            mFlipAnimator.start();
        }
    }

    private boolean isFlipped() {
        return mFlipAnimator.getAnimatedFraction() == 1;
    }

    private boolean isFlipping() {
        final float currentValue = mFlipAnimator.getAnimatedFraction();
        return (currentValue < 1 && currentValue > 0);
    }

    private class FlipListener implements ValueAnimator.AnimatorUpdateListener {

        private final View mFrontView;
        private final View mBackView;
        private boolean mFlipped;

        public FlipListener(final View front, final View back) {
            this.mFrontView = front;
            this.mBackView = back;
            this.mBackView.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationUpdate(final ValueAnimator animation) {
            final float value = animation.getAnimatedFraction();
            final float scaleValue = 0.625f + (1.5f * (value - 0.5f) * (value - 0.5f));

            if(value <= 0.5f){
                this.mFrontView.setRotationY(180 * value);
                this.mFrontView.setScaleX(scaleValue);
                this.mFrontView.setScaleY(scaleValue);
                if(mFlipped){
                    setStateFlipped(false);
                }
            } else {
                this.mBackView.setRotationY(-180 * (1f- value));
                this.mBackView.setScaleX(scaleValue);
                this.mBackView.setScaleY(scaleValue);
                if(!mFlipped){
                    setStateFlipped(true);
                }
            }
        }

        private void setStateFlipped(boolean flipped) {
            mFlipped = flipped;
            this.mFrontView.setVisibility(flipped ? View.GONE : View.VISIBLE);
            this.mBackView.setVisibility(flipped ? View.VISIBLE : View.GONE);
        }
    }
}
