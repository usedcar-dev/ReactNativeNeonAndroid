package com.gaadi.neon.util;

/**
 * Created by lakshaygirdhar
 * @since 13-08-2016
 *
 */

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtils
{

    public static void setIntSharedPreference(Context context, String key, int value) {
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                );
        SharedPreferences.Editor editor = preferences.edit();

        if ((key != null) && !key.isEmpty()) {
            editor.putInt(key, value);
            editor.apply();
        }

    }

    public static void setStringSharedPreference(Context context, String key, String value) {
        SharedPreferences preferences = context.getApplicationContext().
                getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if ((key != null) && !key.isEmpty()) {
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static int getIntSharedPreference(Context context, String key, int defaultValue) {
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                );

        if (preferences.contains(key)) {
            return preferences.getInt(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    public static void setBooleanSharedPreference(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getApplicationContext().
                getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                );
        SharedPreferences.Editor editor = preferences.edit();
        if ((key != null) && !key.isEmpty()) {
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static boolean getBooleanSharedPreference(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = context.getApplicationContext().
                getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                );

        if (preferences.contains(key)) {
            return preferences.getBoolean(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    public static String getStringSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getApplicationContext().
                getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                );

        if (preferences.contains(key)) {
            return preferences.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }


}

