package core;

import android.content.res.Resources;
import android.util.DisplayMetrics;

abstract public class Utils {

    public static float getScreenResolutionRatio() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return ((float) metrics.widthPixels) / ((float) metrics.heightPixels);
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
