package core;

import android.content.res.Resources;
import android.util.DisplayMetrics;

abstract public class Utils {


    private static float resolutionWidth = Utils.getScreenWidth() * Utils.getScreenResolutionRatio();
    private static float resolutionHeight = Utils.getScreenHeight();

    private static float percentX = resolutionWidth / 100;
    private static float percentY = resolutionHeight / 100;

    public static float getPixelsOfPercentX(float percentValue){
        return percentValue * percentX;
    }

    public static float getPixelsOfPercentY(float percentValue){
        return percentValue * percentY;
    }

    public static float getResolutionWidth(){
        return resolutionWidth;
    }

    public static float getResolutionHeight(){
        return resolutionHeight;
    }


    /**
     * Возвращает соотношение сторон дисплея (ширина на высоту)
     */
    public static float getScreenResolutionRatio() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return ((float) metrics.widthPixels) / ((float) metrics.heightPixels);
    }

    /**
     * Возвращает ширину дисплея
     */
    public static int getScreenWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * Возвращает высоту дисплея
     */
    public static int getScreenHeight() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
