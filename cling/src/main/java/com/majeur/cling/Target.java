package com.majeur.cling;

import android.graphics.Point;

public abstract class Target {

    static final class DummyTarget extends Target {

        private final Point mDummyPoint = new Point();

        void set(int x, int y) {
            mDummyPoint.set(x, y);
        }

        @Override
        public Point getLocation() {
            return mDummyPoint;
        }

        @Override
        public int getWidth() {
            return 1;
        }

        @Override
        public int getHeight() {
            return 1;
        }
    }

    public static final Target NONE = new DummyTarget();

    public abstract Point getLocation();

    public abstract int getWidth();

    public abstract int getHeight();

    Point getCenterLocation() {
        Point p = getLocation();
        return new Point(p.x + getWidth() / 2, p.y + getHeight() / 2);
    }
}
