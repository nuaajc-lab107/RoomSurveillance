package com.aye10032.roomsurveillance;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class OnGestureListener implements GestureDetector.OnGestureListener {

    final int FLING_MIN_DISTANCE = 50;
    final float FLING_MIN_VELOCITY = 50;

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
