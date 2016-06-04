package com.majeur.cling;

import android.app.Activity;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;

public class ClingManager implements View.OnClickListener {

    private Queue<Cling> mClingQueue = new LinkedList<>();

    private Callbacks mCallbacks;
    private int mCurrentIndex;

    private Activity mActivity;
    private ClingView mClingView;
    private boolean mIsStarted;

    public ClingManager(Activity activity) {
        mActivity = activity;
    }

    public void addCling(Cling cling) {
        mClingQueue.offer(cling);
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public void start() {
        if (mIsStarted)
            return;

        mIsStarted = true;
        mCurrentIndex = -1;

        mClingView = new ClingView(mActivity);
        mClingView.setOnClickListener(this);
        mClingView.addInWindow(mActivity.getWindow());
        showNext();
    }

    private void stop() {
        if (!mIsStarted)
            return;

        while (mClingQueue.poll() != null)
            mCurrentIndex++;

        mIsStarted = false;
        mClingView.removeFromWindow(mActivity.getWindow());
        mClingView = null;
    }

    private void showNext() {
        Cling cling = mClingQueue.poll();
        mCurrentIndex++;

        if (cling != null) {
            mClingView.setCling(cling);

            mClingView.show(new Runnable() {
                @Override
                public void run() {
                    if (mCallbacks != null)
                        mCallbacks.onClingShow(mCurrentIndex);
                }
            });
        } else {
            stop();
        }
    }

    @Override
    public void onClick(View view) {
        if (mCallbacks == null || !mCallbacks.onClingClick(mCurrentIndex)) {
            mClingView.hide(new Runnable() {
                @Override
                public void run() {
                    if (mCallbacks != null)
                        mCallbacks.onClingHide(mCurrentIndex);

                    showNext();
                }
            });
        }
    }

    public static abstract class Callbacks {

        public boolean onClingClick(int position) {
            return false;
        }

        public void onClingShow(int position) {

        }

        public void onClingHide(int position) {

        }
    }
}
