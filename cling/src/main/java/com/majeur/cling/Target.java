package com.majeur.cling;

import android.graphics.Point;
import android.util.Log;

public abstract class Target {

    public static final Target NONE = new Target() {
        @Override
        public Point getLocation() {
            return null;
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }
    };

    public abstract Point getLocation();

    public abstract int getWidth();

    public abstract int getHeight();

    Point getCenterLocation() {
        Point p = getLocation();
        return new Point(p.x + getWidth() / 2, p.y + getHeight() / 2);
    }

    boolean isValid() {
        final boolean isValid = getWidth() != 0 && getHeight() != 0;
        Log.i(getClass().getSimpleName(), "Target " + getClass().getSimpleName() + " isn't valid.");
        return isValid;
    }

}
