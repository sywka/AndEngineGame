package com.el.game.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
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

    /**
     * Сохранить параметр типа bool в конфигурационном файле
     *
     * @param context контекст приложения
     * @param key     ключ параметра
     * @param value   значение параметра
     */
    public static void save(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Сохранить параметр типа int в конфигурационном файле
     *
     * @param context контекст приложения
     * @param key     ключ параметра
     * @param value   значение параметра
     */
    public static void save(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Сохранить параметр типа float в конфигурационном файле
     *
     * @param context контекст приложения
     * @param key     ключ параметра
     * @param value   значение параметра
     */
    public static void save(Context context, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Загрузить параметр типа bool из конфигурационного файла
     *
     * @param context  контекст приложения
     * @param key      ключ параметра
     * @param defValue дефолтное значение параметра. Если конфигурационный файл не содержит ключа key возвращает defValue
     * @return параметр по ключю.
     */
    public static boolean load(Context context, String key, boolean defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, defValue);
    }

    /**
     * Загрузить параметр типа int из конфигурационного файла
     *
     * @param context  контекст приложения
     * @param key      ключ параметра
     * @param defValue дефолтное значение параметра. Если конфигурационный файл не содержит ключа key возвращает defValue
     * @return параметр по ключю.
     */
    public static int load(Context context, String key, int defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, defValue);
    }

    /**
     * Загрузить параметр типа int из конфигурационного файла
     *
     * @param context  контекст приложения
     * @param key      ключ параметра
     * @param defValue дефолтное значение параметра. Если конфигурационный файл не содержит ключа key возвращает defValue
     * @return параметр по ключю.
     */
    public static float load(Context context, String key, float defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getFloat(key, defValue);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        return editor;
    }
}
