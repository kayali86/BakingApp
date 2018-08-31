package com.kayali_developer.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.utilities.RecipeJSONUtils;

import java.util.List;

public class WidgetRecipeLoadService extends IntentService {

    public WidgetRecipeLoadService() {
        super("WidgetRecipeLoadService");
    }

    // Start WidgetRecipeLoadService to load and update recipes data
    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, WidgetRecipeLoadService.class);
        intent.setAction(AppConstants.ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        // If Action = ACTION_UPDATE_RECIPE_WIDGETS Start WidgetRecipeLoadService to load and update recipes data
        if (intent.getAction().equals(AppConstants.ACTION_UPDATE_RECIPE_WIDGETS)) {
            List<Recipe> recipes = RecipeJSONUtils.extractRecipesData(AppConstants.REQUEST_URL);
            BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes, -1);

        // If action = ACTION_PREVIOUS_RECIPE move to previous Recipe and update widget
        } else if (intent.getAction().equals(AppConstants.ACTION_PREVIOUS_RECIPE)) {
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

            // If action = ACTION_GET_RECIPES load all recipes and pass value to GridRemoteViewsFactory
        } else if (intent.getAction().equals(AppConstants.ACTION_GET_RECIPES)) {
            List<Recipe> recipes = RecipeJSONUtils.extractRecipesData(AppConstants.REQUEST_URL);
            GridRemoteViewsFactory.getRecipes(recipes);
            BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes, -1);
        }
    }
}
