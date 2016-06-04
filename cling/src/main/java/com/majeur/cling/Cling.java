package com.majeur.cling;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class Cling {

    private Cling(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.textAppearanceLarge});
        titleTextAppearance = typedArray.getResourceId(0, -1);
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.textAppearanceMedium});
        contentTextAppearance = typedArray.getResourceId(0, -1);
        typedArray.recycle();

    }

    String title;
    String content;
    int clingColor = 0x90000000;
    int messageBackground = 0xFF1976D2;
    Target target = Target.NONE;
    int titleTextAppearance;
    int contentTextAppearance;

    public static class Builder {

        private Resources mResources;
        private Cling mCling;

        public Builder(Context context) {
            mResources = context.getResources();
            mCling = new Cling(context);
        }

        public Builder setTitle(String title) {
            mCling.title = title;
            return this;
        }

        public Builder setTitle(int resId) {
            return setTitle(mResources.getString(resId));
        }

        public Builder setContent(String content) {
            mCling.content = content;
            return this;
        }

        public Builder setContent(int resId) {
            return setContent(mResources.getString(resId));
        }

        public Builder setClingColor(int clingColor) {
            mCling.clingColor = clingColor;
            return this;
        }

        public Builder setMessageBackground(int messageBackground) {
            mCling.messageBackground = messageBackground;
            return this;
        }

        public Builder setTarget(Target target) {
            mCling.target = target;
            return this;
        }

        public Builder setTitleTextAppearance(int resId) {
            mCling.titleTextAppearance = resId;
            return this;
        }

        public Builder setContentTextAppearance(int resId) {
            mCling.contentTextAppearance = resId;
            return this;
        }

        public Cling build() {
            return mCling;
        }

    }

}
