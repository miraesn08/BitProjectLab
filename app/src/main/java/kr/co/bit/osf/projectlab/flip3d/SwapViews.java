package kr.co.bit.osf.projectlab.flip3d;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

// http://www.inter-fuser.com/2009/08/android-animations-3d-flip.html
public final class SwapViews implements Runnable {
    private boolean mIsFirstView;
    View view1;
    View view2;

    public SwapViews(boolean isFirstView, View view1, View view2) {
        mIsFirstView = isFirstView;
        this.view1 = view1;
        this.view2 = view2;
    }

    public void run() {
        final float centerX = view1.getWidth() / 2.0f;
        final float centerY = view1.getHeight() / 2.0f;
        Flip3dAnimation rotation;

        if (mIsFirstView) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            view2.requestFocus();

            rotation = new Flip3dAnimation(-90, 0, centerX, centerY);
        } else {
            view2.setVisibility(View.GONE);
            view1.setVisibility(View.VISIBLE);
            view1.requestFocus();

            rotation = new Flip3dAnimation(90, 0, centerX, centerY);
        }

        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());

        if (mIsFirstView) {
            view2.startAnimation(rotation);
        } else {
            view1.startAnimation(rotation);
        }
    }
}