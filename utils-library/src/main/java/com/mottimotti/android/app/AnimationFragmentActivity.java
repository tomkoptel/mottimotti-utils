package com.mottimotti.android.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.mottimotti.android.R;


public class AnimationFragmentActivity extends FragmentActivity {
    public static final int FROM_RIGHT_TO_LEFT = 101;
    public static final int FROM_LEFT_TO_RIGHT = 102;
    public static final int FROM_TOP_TO_BOTTOM = 103;
    public static final int FROM_BOTTOM_TO_TOP = 104;
    private int animationFlag = -1;

    public void startActivityForResultWithAnimation(Intent intent, int flag, int animationType) {
        super.startActivityForResult(intent, flag);
        overrideTransactions(animationType);
    }

    public void startActivityWithAnimation(Intent intent, int animationType) {
        super.startActivityForResult(intent, 500);
        overrideTransactions(animationType);
    }

    private void overrideTransactions(int animationType) {
        animationFlag = animationType;
        switch (animationType) {
            case FROM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case FROM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case FROM_TOP_TO_BOTTOM:
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                break;
            case FROM_BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overrideRevertTransactions(animationFlag);
    }

    private void overrideRevertTransactions(int animationType) {
        animationFlag = animationType;
        switch (animationType) {
            case FROM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case FROM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case FROM_TOP_TO_BOTTOM:
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;
            case FROM_BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                break;
        }
    }
}
