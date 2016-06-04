package com.majeur.cling;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;

import java.lang.ref.WeakReference;

public class ViewTarget extends Target {

    private WeakReference<View> mViewWeakReference;

    public ViewTarget(View v) {
        mViewWeakReference = new WeakReference<View>(v);
    }

    public ViewTarget(Activity activity, int viewId) {
        this(activity.findViewById(viewId));
    }

    @Override
    public Point getLocation() {
        final int[] loc = new int[2];
        mViewWeakReference.get().getLocationOnScreen(loc);
        return new Point(loc[0], loc[1]);
    }

    @Override
    public int getWidth() {
        return mViewWeakReference.get() != null ? mViewWeakReference.get().getWidth() : 0;
    }

    @Override
    public int getHeight() {
        return mViewWeakReference.get() != null ? mViewWeakReference.get().getHeight() : 0;
    }
}
