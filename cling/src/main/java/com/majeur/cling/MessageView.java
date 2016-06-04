package com.majeur.cling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

class MessageView extends LinearLayout {

    private Paint mPaint;
    private RectF mRect;

    private TextView mTitleTextView;
    private TextView mContentTextView;

    MessageView(Context context) {
        this(context, null);
    }

    MessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float density = context.getResources().getDisplayMetrics().density;

        setWillNotDraw(false);

        mRect = new RectF();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        float radius = density * 3.0f;
        float dy = density * 2f;
        mPaint.setShadowLayer(radius, 0, dy, 0xFF3D3D3D);

        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        final int padding = (int) (10 * density);

        mTitleTextView = new TextView(context);
        mTitleTextView.setPadding(padding, padding, padding, padding);
        mTitleTextView.setGravity(Gravity.CENTER);
        addView(mTitleTextView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mContentTextView = new TextView(context);
        mContentTextView.setPadding(padding, padding, padding, padding);
        mContentTextView.setGravity(Gravity.CENTER);
        addView(mContentTextView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    void setTitleTextAppearance(int resId) {
        mTitleTextView.setTextAppearance(getContext(), resId);
        mTitleTextView.setTextColor(getDecoredColor());
    }

    public void setContentText(String content) {
        mContentTextView.setText(content);
    }

    void setContentTextAppearance(int resId) {
        mContentTextView.setTextAppearance(getContext(), resId);
        mContentTextView.setTextColor(getDecoredColor());
    }

    private int getDecoredColor() {
        final int color =  mPaint.getColor();
        return color == -1 ? Color.WHITE : (isDark(color) ? Color.WHITE : Color.DKGRAY);
    }

    private boolean isDark(int color) {
        float darkness = (0.299f * Color.red(color) + 0.587f * Color.green(color) + 0.114f * Color.blue(color)) / 255;
        return darkness <= 0.75f;
    }

    public void setColor(int color) {
        if (color == -1) {
            mPaint.setAlpha(0);
        } else {
            mPaint.setAlpha(255);
            mPaint.setColor(color);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRect.set(getPaddingLeft(),
                getPaddingTop(),
                canvas.getWidth() - getPaddingRight(),
                canvas.getHeight() - getPaddingBottom());

        canvas.drawRoundRect(mRect, 3, 3, mPaint);
    }
}
