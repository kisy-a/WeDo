package com.example.wedoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class preferences {
    private static final String DATA_LOGIN = "status_login",
            DATA_AS = "as", DATA_SORT_BY="", DATA_RATING_BY="", DATA_MIN_PRICE="", DATA_MAX_PRICE="";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setDataAs(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATA_AS,data);
        editor.apply();
    }
    public static void setDataSortBy(Context context, String datasort){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATA_SORT_BY,datasort);
        editor.apply();
    }
    public static void setDataRatingBy(Context context, String datarating){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATA_RATING_BY,datarating);
        editor.apply();
    }
    public static void setDataMinPrice(Context context, String datamin){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATA_MIN_PRICE,datamin);
        editor.apply();
    }
    public static void setDataMaxPrice(Context context, String datamax){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATA_MAX_PRICE,datamax);
        editor.apply();
    }

    public static String getDataAs(Context context){
        return getSharedPreferences(context).getString(DATA_AS,"");
    }

    public static String getDataSortBy(Context context){
        return getSharedPreferences(context).getString(DATA_SORT_BY,"");
    }
    public static String getDataRatingBy(Context context){
        return getSharedPreferences(context).getString(DATA_RATING_BY,"");
    }
    public static String getDataMinPrice(Context context){
        return getSharedPreferences(context).getString(DATA_MIN_PRICE,"");
    }
    public static String getDataMaxPrice(Context context){
        return getSharedPreferences(context).getString(DATA_MAX_PRICE,"");
    }

    public static void setDataLogin(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(DATA_LOGIN,status);
        editor.apply();
    }

    public static boolean getDataLogin(Context context){
        return getSharedPreferences(context).getBoolean(DATA_LOGIN,false);
    }

    public static void clearData(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(DATA_AS);
        editor.remove(DATA_LOGIN);

        editor.remove(DATA_SORT_BY);
        editor.remove(DATA_RATING_BY);
        editor.remove(DATA_MIN_PRICE);
        editor.remove(DATA_MAX_PRICE);
        editor.apply();
    }


}