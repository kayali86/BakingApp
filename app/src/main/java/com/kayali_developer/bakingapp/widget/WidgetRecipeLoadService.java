package com.kayali_developer.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.api.ApiCallback;
import com.kayali_developer.bakingapp.api.ApiManager;
import com.kayali_developer.bakingapp.data.model.Ingredient;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.utilities.RecipeJSONUtils;
import com.kayali_developer.bakingapp.utilities.RecipeNetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetRecipeLoadService extends IntentService {
    public WidgetRecipeLoadService() {
        super("WidgetRecipeLoadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
            // If action = ACTION_PREVIOUS_RECIPE move to previous Recipe and update widget
        if (intent.getAction().equals(AppConstants.ACTION_PREVIOUS_RECIPE)) {
            int position = intent.getExtras().getInt(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET);
            List<Recipe> recipes = intent.getParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET);
            int newRecipePosition = position;
            if (position > 0) {
                newRecipePosition--;
            }

            BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes, newRecipePosition);
            // If action = ACTION_NEXT_RECIPE move to next Recipe and update widget
        } else if (intent.getAction().equals(AppConstants.ACTION_NEXT_RECIPE)) {
            int position = intent.getExtras().getInt(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET);
            List<Recipe> recipes = intent.getParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET);
            int newRecipePosition = position;
            if (position < recipes.size() - 1) {
                newRecipePosition++;
            }
            BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes, newRecipePosition);
        }
    }
}
