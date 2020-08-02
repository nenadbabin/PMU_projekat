package com.example.pmu_projekat.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private final static String SHARED_PREFERENCES_FILE_NAME = "rs.etf.pmu.storage";

    public static void save (Context context, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void save (Context context, String key, int value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void save (Context context, String key, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void save (Context context, String key, float value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static int getInt (Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int anInt = sharedPreferences.getInt(key, -1000);
        editor.commit();

        return anInt;
    }

    public static String getString (Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String error = sharedPreferences.getString(key, "error");
        editor.commit();

        return error;
    }
}
