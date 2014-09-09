package core;

import android.content.res.Resources;
import android.util.DisplayMetrics;

abstract public class Utils {

    /** Возвращает соотношение сторон дисплея (ширина на высоту) */
    public static float getScreenResolutionRatio() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return ((float) metrics.widthPixels) / ((float) metrics.heightPixels);
    }

    /** Возвращает ширину дисплея */
    public static int getScreenWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /** Возвращает высоту дисплея */
    public static int getScreenHeight() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
