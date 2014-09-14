package com.el.game.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.el.game.R;

abstract public class Utils {

    private static float resolutionWidth;
    private static float resolutionHeight;

    private static float percentX;
    private static float percentY;

    public static void calculateResolution(Context context) {
        resolutionWidth = Utils.getScreenWidth() * Utils.getScreenResolutionRatio(context);
        resolutionHeight = Utils.getScreenHeight() -
                context.getResources().getDimension(R.dimen.header_menu_height) -
                context.getResources().getDimension(R.dimen.footer_menu_height);

        percentX = resolutionWidth / 100;
        percentY = resolutionHeight / 100;
    }

    public static float getPixelsOfPercentX(float percentValue) {
        return percentValue * percentX;
    }

    public static float getPixelsOfPercentY(float percentValue) {
        return percentValue * percentY;
    }

    public static float getResolutionWidth() {
        return resolutionWidth;
    }

    public static float getResolutionHeight() {
        return resolutionHeight;
    }


    /**
     * Возвращает соотношение сторон дисплея (ширина на высоту)
     */
    public static float getScreenResolutionRatio(Context context) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return ((float) metrics.widthPixels) / ((float) metrics.heightPixels -
                context.getResources().getDimension(R.dimen.header_menu_height) -
                context.getResources().getDimension(R.dimen.footer_menu_height));
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
