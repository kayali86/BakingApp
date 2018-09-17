package com.kayali_developer.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.activity.RecipeActivity;
import com.kayali_developer.bakingapp.api.ApiCallback;
import com.kayali_developer.bakingapp.api.ApiManager;
import com.kayali_developer.bakingapp.data.model.Ingredient;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.utilities.Prefs;
import com.kayali_developer.bakingapp.utilities.RecipeNetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class BakingAppWidget extends AppWidgetProvider {
    static int mCurrentRecipePosition = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, List<Recipe> recipes, int newRecipePosition) {
        RemoteViews views;
        // Detect if widget is a single Widget or GridView Widget
        views = getSinglePlantRemoteView(context, recipes, newRecipePosition, appWidgetId, appWidgetManager);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        loadRecipes(context, appWidgetManager, appWidgetIds);
    }


    private void loadRecipes(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        if (RecipeNetworkUtils.isConnected(context)) {
            ApiManager.getInstance().getRecipes(new ApiCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> recipes) {
                    if (recipes != null && recipes.size() > 0) {
                        updateRecipeWidgets(context, appWidgetManager, appWidgetIds, recipes, mCurrentRecipePosition);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_data_to_display), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(context, context.getResources().getString(R.string.loading_canceled), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                           List<Recipe> recipes, int newRecipePosition) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipes, newRecipePosition);
        }
    }

    /**
     * @param newRecipePosition The new retrieved Recipe's position after next or previous button is clicked
     */
    private static RemoteViews getSinglePlantRemoteView(Context context, List<Recipe> recipes, int newRecipePosition,
                                                        int appWidgetId, AppWidgetManager appWidgetManager) {

        // newRecipePosition has value -1 when it has no changes after next or previous button is clicked
        if (newRecipePosition != -1) {
            mCurrentRecipePosition = newRecipePosition;
            Prefs.saveCurrentRecipePosition(context, newRecipePosition);
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        Recipe currentRecipe = recipes.get(mCurrentRecipePosition);

        if (currentRecipe != null) {
            // Intent to open current recipe details in RecipeActivity when clicked
            Intent appIntent = new Intent(context, RecipeActivity.class);
            appIntent.putExtra(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Construct the RemoteViews object

            views.setTextViewText(R.id.tv_recipe_name, currentRecipe.getName());
            // Widgets allow click handlers to only launch pending intents
            views.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);

            // Initialize the list view
            Intent intent = new Intent(context, WidgetService.class);
            String extra =  new Gson().toJson(recipes);
            intent.putExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, extra);

            // Bind the remote adapter
            views.setRemoteAdapter(R.id.recipe_widget_listview, intent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);


            // Intent to handle previous recipe button function
            Intent previousRecipeIntent = new Intent(context, WidgetRecipeLoadService.class);
            previousRecipeIntent.setAction(AppConstants.ACTION_PREVIOUS_RECIPE);
            previousRecipeIntent.putExtra(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, mCurrentRecipePosition);
            previousRecipeIntent.putParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
            PendingIntent previousRecipePendingIntent = PendingIntent.getService(context, 0, previousRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.iv_previous_nav, previousRecipePendingIntent);

            // Intent to handle next recipe button function
            Intent nextRecipeIntent = new Intent(context, WidgetRecipeLoadService.class);
            nextRecipeIntent.setAction(AppConstants.ACTION_NEXT_RECIPE);
            nextRecipeIntent.putExtra(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, mCurrentRecipePosition);
            nextRecipeIntent.putParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
            PendingIntent nextRecipePendingIntent = PendingIntent.getService(context, 0, nextRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.iv_next_nav, nextRecipePendingIntent);
        }
        return views;
        /*
        int mCurrentRecipePosition = 0;
        // newRecipePosition has value -1 when it has no changes after next or previous button is clicked
        if (newRecipePosition != -1) {
            mCurrentRecipePosition = newRecipePosition;
        }
        Recipe currentRecipe = recipes.get(mCurrentRecipePosition);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.tv_recipe_name, currentRecipe.getName());

        // Intent to handle previous recipe button function
        Intent previousRecipeIntent = new Intent(context, WidgetRecipeLoadService.class);
        previousRecipeIntent.setAction(AppConstants.ACTION_PREVIOUS_RECIPE);
        previousRecipeIntent.putExtra(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, mCurrentRecipePosition);
        previousRecipeIntent.putParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
        PendingIntent previousRecipePendingIntent = PendingIntent.getService(context, 0, previousRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.iv_previous_nav, previousRecipePendingIntent);

        // Intent to handle next recipe button function
        Intent nextRecipeIntent = new Intent(context, WidgetRecipeLoadService.class);
        nextRecipeIntent.setAction(AppConstants.ACTION_NEXT_RECIPE);
        nextRecipeIntent.putExtra(AppConstants.CURRENT_RECIPE_POSITION_KEY_WIDGET, mCurrentRecipePosition);
        nextRecipeIntent.putParcelableArrayListExtra(AppConstants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
        PendingIntent nextRecipePendingIntent = PendingIntent.getService(context, 0, nextRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.iv_next_nav, nextRecipePendingIntent);
*/

    }

}


