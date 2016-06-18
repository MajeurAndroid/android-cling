package com.majeur.cling;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

public class ActionItemTarget extends Target {

    private final WeakReference<Activity> mActivityReference;
    private final int mActionId;

    // For action items, we have to search for the view at method calls, and not in constructor.
    public ActionItemTarget(Activity activity, int actionItemId) {
        mActivityReference = new WeakReference<>(activity);
        mActionId = actionItemId;
    }

    @Override
    public Point getLocation() {
        if (mActivityReference.get() == null)
            return new Point();

        View actionView = mActivityReference.get().findViewById(mActionId);
        if (actionView == null) {
            Log.e("Cling", "ActionItemTarget with id " + Integer.toHexString(mActionId) + " do not point to anything, make sure " +
                    "the option item exists, and has the attribute: showAsAction=\"always\"");
            return new Point();
        }

        final int[] loc = new int[2];
        actionView.getLocationOnScreen(loc);
        return new Point(loc[0], loc[1]);
    }

    @Override
    public int getWidth() {
        View view;
        return (mActivityReference.get() != null
                && (view = mActivityReference.get().findViewById(mActionId)) != null) ? view.getWidth() : 0;
    }

    @Override
    public int getHeight() {
        View view;
        return (mActivityReference.get() != null
                && (view = mActivityReference.get().findViewById(mActionId)) != null) ? view.getHeight() : 0;
    }
}
