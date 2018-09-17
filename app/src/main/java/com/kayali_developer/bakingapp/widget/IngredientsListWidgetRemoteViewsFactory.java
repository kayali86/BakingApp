package com.kayali_developer.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.data.model.Ingredient;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.utilities.Prefs;
import com.kayali_developer.bakingapp.utilities.RecipeJSONUtils;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private static List<Ingredient> mIngredients = new ArrayList<>();
    private int mCurrentRecipePosition = 0;
    private List<Recipe> mRecipes;


    IngredientsListWidgetRemoteViewsFactory(Context applicationContext, String recipesStr) {
        mContext = applicationContext;
        mRecipes = RecipeJSONUtils.extractRecipesData(recipesStr);
        Recipe currentRecipe = mRecipes.get(mCurrentRecipePosition);
        mIngredients = currentRecipe.getIngredients();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mCurrentRecipePosition = Prefs.getCurrentRecipePosition(mContext);
        Recipe currentRecipe = mRecipes.get(mCurrentRecipePosition);
        mIngredients = currentRecipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.size() == 0) return null;
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_item);
        views.setTextViewText(R.id.tv_ingredient_widget, mIngredients.get(position).getIngredient());
        views.setTextViewText(R.id.tv_quantity_widget, String.valueOf(mIngredients.get(position).getQuantity()));
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
