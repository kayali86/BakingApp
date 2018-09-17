package com.kayali_developer.bakingapp.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.api.ApiCallback;
import com.kayali_developer.bakingapp.api.ApiManager;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.utilities.RecipeJSONUtils;
import com.kayali_developer.bakingapp.utilities.RecipeNetworkUtils;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private Context mContext;
    private MutableLiveData<List<Recipe>> mRecipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        if (mRecipes == null) {
            mRecipes = new MutableLiveData<>();
            loadRecipes();
        }
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    private void loadRecipes() {
        /*
        if (RecipeNetworkUtils.isConnected(mContext)) {
            RecipeAsyncTask task = new RecipeAsyncTask();
            task.execute(AppConstants.REQUEST_URL);

        }
*/
        if (RecipeNetworkUtils.isConnected(mContext)) {
            ApiManager.getInstance().getRecipes(new ApiCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> recipes) {
                    if (recipes != null && recipes.size() > 0) {
                        setLoadedRecipesToLiveDataObject(recipes);
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.no_data_to_display), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancel() {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.loading_canceled), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }
/*
    // AsyncTask class to load Recipes data on Background
    private class RecipeAsyncTask extends AsyncTask<String, Void, List<Recipe>> {
        @Override
        protected List<Recipe> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            return RecipeJSONUtils.extractRecipesData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            if (recipes != null && !recipes.isEmpty()) {
                setLoadedRecipesToLiveDataObject(recipes);
            }
        }

        // Helper method to set retrieved data to MutableLiveData<List<Recipe>> mRecipes
        private void setLoadedRecipesToLiveDataObject(List<Recipe> recipes) {
            mRecipes.setValue(recipes);
        }
    }
*/
    // Helper method to set retrieved data to MutableLiveData<List<Recipe>> mRecipes
    private void setLoadedRecipesToLiveDataObject(List<Recipe> recipes) {
        mRecipes.setValue(recipes);
    }
}
