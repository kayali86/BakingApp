package com.kayali_developer.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private static List<Recipe> mRecipes = new ArrayList<>();

    GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (mRecipes.size() == 0) {
            // Start WidgetRecipeLoadService to load all Recipes
            Intent intent = new Intent(mContext, WidgetRecipeLoadService.class);
            intent.setAction(AppConstants.ACTION_GET_RECIPES);
            mContext.startService(intent);
        }
    }

    // Get recipes from WidgetRecipeLoadService
    public static void getRecipes(List<Recipe> recipes) {
        if (mRecipes.size() == 0) {
            mRecipes.addAll(recipes);
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipes == null || mRecipes.size() == 0) return null;
        Recipe currentRecipe = mRecipes.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.tv_recipe_name, currentRecipe.getName());
        views.setTextViewText(R.id.tv_recipe_nr, String.valueOf(currentRecipe.getId()));
        views.setTextViewText(R.id.tv_recipe_servings, String.valueOf(currentRecipe.getServings()));
        views.setViewVisibility(R.id.iv_previous_nav, View.GONE);
        views.setViewVisibility(R.id.iv_next_nav, View.GONE);

        Bundle extras = new Bundle();
        extras.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.current_recipe_layout, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
