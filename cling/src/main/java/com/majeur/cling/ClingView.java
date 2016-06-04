package com.majeur.cling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.AbsoluteLayout;

class ClingView extends AbsoluteLayout {

    private static final Xfermode XFERMODE_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private static final Interpolator INTERPOLATOR = new PathInterpolator(.1f, .7f, .1f, 1f);
    private static final int CLING_ANIM_DURATION = 750;
    private static final int MESSAGE_ANIM_DURATION = 350;

    private Target mTarget;
    private Point mTargetPoint;

    private Paint mPaint;
    private MessageView mMessageView;
    private Path mPath;
    private Bitmap mTargetBitmap;

    private int mClingColor;
    private boolean mFistLayoutPassed;

    ClingView(Context context) {
        super(context);

        final float density = context.getResources().getDisplayMetrics().density;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();

        setWillNotDraw(false);

        mMessageView = new MessageView(context);
        final int padding = (int) (10 * density);
        mMessageView.setPadding(padding, padding, padding, padding);
        addView(mMessageView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0));

        setAlpha(0f);
        mMessageView.setAlpha(0f);

        animate().setDuration(CLING_ANIM_DURATION).setInterpolator(INTERPOLATOR);
        mMessageView.animate().setDuration(MESSAGE_ANIM_DURATION).setInterpolator(INTERPOLATOR);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getViewTreeObserver().isAlive())
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mFistLayoutPassed = true;
                updateTargetInfo();
            }
        });
    }

    void addInWindow(Window window) {
        ((ViewGroup) window.getDecorView()).addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setVisibility(INVISIBLE);
    }

    void removeFromWindow(Window window) {
        ((ViewGroup) window.getDecorView()).removeView(this);
    }

    void show(final Runnable whenDidShow) {
        updateTargetInfo();

        animate()
                .alpha(1f)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        whenDidShow.run();
                    }
                });

        mMessageView.animate()
                .alpha(1f)
                .setStartDelay(CLING_ANIM_DURATION)
                .withEndAction(null);
    }

    void hide(final Runnable whenDidHide) {
        animate()
                .alpha(0f)
                .withStartAction(null)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(INVISIBLE);
                        whenDidHide.run();
                    }
                });

        mMessageView.animate()
                .alpha(0f)
                .setStartDelay(0);
    }

    void updateTargetInfo() {
        if (!mFistLayoutPassed)
            return;

        if (mTargetBitmap != null)
            mTargetBitmap.recycle();

        if (mTarget instanceof Target.DummyTarget)
            ((Target.DummyTarget) mTarget).set(getWidth(), getHeight());

        final int targetWidth = (int) Math.min(mTarget.getWidth(), getWidth() * 0.6666f);
        final int targetHeight = (int) Math.min(mTarget.getHeight(), getWidth() * 0.6666f);
        final float size = Math.max(targetWidth, targetHeight) * 1.2f;

        mTargetBitmap = Bitmap.createBitmap((int) size, (int) size, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(mTargetBitmap);
        canvas.drawColor(mClingColor);
        mPaint.setXfermode(XFERMODE_CLEAR);
        canvas.drawCircle(size / 2, size / 2, size / 2, mPaint);
        mPaint.setXfermode(null);

        mTargetPoint = mTarget.getCenterLocation();
        invalidate();

        LayoutParams params = (LayoutParams) mMessageView.getLayoutParams();
        Point p = resolveMessageViewLocation();
        params.x = p.x;
        params.y = p.y;
        requestLayout();
    }

    void setCling(Cling cling) {
        mMessageView.setTitle(cling.title);
        mMessageView.setTitleTextAppearance(cling.titleTextAppearance);
        mMessageView.setContentText(cling.content);
        mMessageView.setContentTextAppearance(cling.contentTextAppearance);
        mMessageView.setColor(cling.messageBackground);

        mTarget = cling.target;

        mClingColor = cling.clingColor;
        mPaint.setColor(cling.clingColor);
        mPaint.setAlpha(Color.alpha(cling.clingColor));
    }

    private Point resolveMessageViewLocation() {
        int x;
        if (mTargetPoint.x > getWidth() / 2)
            x = mTargetPoint.x / 2 - mMessageView.getWidth() / 2;
        else
            x = mTargetPoint.x + (getWidth() - mTargetPoint.x) / 2 - mMessageView.getWidth() / 2;

        if (x + mMessageView.getWidth() > getWidth())
            x -= (x + mMessageView.getWidth() - getWidth());
        if (x < 0)
            x = 0;

        int y;
        if (mTargetPoint.y > getHeight() / 2)
            y = mTargetPoint.y / 2 - mMessageView.getHeight() / 2;
        else
            y = mTargetPoint.y + (getHeight() - mTargetPoint.y) / 2 - mMessageView.getHeight() / 2;

        if (y + mMessageView.getHeight() > getHeight())
            y -= (y + mMessageView.getHeight() - getHeight());
        if (y < 0)
            y = 0;

        return new Point(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float bmpX = mTargetPoint.x - mTargetBitmap.getWidth() / 2;
        float bmpY = mTargetPoint.y - mTargetBitmap.getHeight() / 2;
        canvas.drawBitmap(mTargetBitmap, bmpX, bmpY, null);

        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(canvas.getWidth(), 0);
        mPath.lineTo(canvas.getWidth(), bmpY);
        mPath.lineTo(bmpX, bmpY);
        mPath.lineTo(bmpX, bmpY + mTargetBitmap.getHeight());
        mPath.lineTo(bmpX + mTargetBitmap.getWidth(), bmpY + mTargetBitmap.getHeight());
        mPath.lineTo(bmpX + mTargetBitmap.getWidth(), bmpY);
        mPath.lineTo(canvas.getWidth(), bmpY);
        mPath.lineTo(canvas.getWidth(), canvas.getHeight());
        mPath.lineTo(0, canvas.getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
