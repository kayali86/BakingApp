package com.kayali_developer.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.activity.RecipeActivity;
import com.kayali_developer.bakingapp.adapter.IngredientsAdapter;
import com.kayali_developer.bakingapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class BakingAppWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, List<Recipe> recipes, int newRecipePosition) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews views;
        // Detect if widget is a single Widget or GridView Widget
        if (width < 200) {
            views = getSinglePlantRemoteView(context, recipes, newRecipePosition);
        } else {
            views = getGardenGridRemoteView(context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetRecipeLoadService.startActionUpdateRecipeWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<Recipe> recipes, int newRecipePosition) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipes, newRecipePosition);
        }
    }

    /**
    *@param newRecipePosition The new retrieved Recipe's position after next or previous button is clicked
     */
    private static RemoteViews getSinglePlantRemoteView(Context context, List<Recipe> recipes, int newRecipePosition) {
        int currentRecipePosition = 0;
        // newRecipePosition has value -1 when it has no changes after next or previous button is clicked
        if (newRecipePosition != -1) {
            currentRecipePosition = newRecipePosition;
        }
        Recipe currentRecipe = recipes.get(currentRecipePosition);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.tv_recipe_name, currentRecipe.getName());
        views.setTextViewText(R.id.tv_recipe_nr, String.valueOf(currentRecipe.getId()));
        views.setTextViewText(R.id.tv_recipe_servings, String.valueOf(currentRecipe.getServings()));

        // Intent to handle previous recipe button function
        Intent previousRecipeIntent = new Intent(context, WidgetRecipeLoadService.class);
        previousRecipeIntent.setAction(AppConstants.ACTION_PREVIOUS_RECIPE);
        previousRecipeIntent.putExtra(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, currentRecipePosition);
        previousRecipeIntent.putParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
        PendingIntent previousRecipePendingIntent = PendingIntent.getService(context, 0, previousRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.iv_previous_nav, previousRecipePendingIntent);

        // Intent to handle next recipe button function
        Intent nextRecipeIntent = new Intent(context, WidgetRecipeLoadService.class);
        nextRecipeIntent.setAction(AppConstants.ACTION_NEXT_RECIPE);
        nextRecipeIntent.putExtra(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, currentRecipePosition);
        nextRecipeIntent.putParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
        PendingIntent nextRecipePendingIntent = PendingIntent.getService(context, 0, nextRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.iv_next_nav, nextRecipePendingIntent);

        // Intent to open current recipe details in RecipeActivity when clicked
        Intent appIntent = new Intent(context, RecipeActivity.class);
        appIntent.putExtra(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.current_recipe_layout, pendingIntent);
        return views;
    }

    private static RemoteViews getGardenGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_grid);
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        // Intent to open current recipe details in RecipeActivity when clicked
        Intent appIntent = new Intent(context, RecipeActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.widget_empty_view);
        return views;
    }
}


