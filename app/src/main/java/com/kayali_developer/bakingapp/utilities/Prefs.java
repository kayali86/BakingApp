package com.kayali_developer.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.kayali_developer.bakingapp.AppConstants;

public class Prefs {
    public static void saveCurrentRecipePosition(Context context, int currentRecipePosition) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putInt(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, currentRecipePosition);
        prefs.apply();
    }

    public static int getCurrentRecipePosition(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, 0);
    }
}
