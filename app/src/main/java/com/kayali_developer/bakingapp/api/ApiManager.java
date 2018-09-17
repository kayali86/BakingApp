package com.kayali_developer.bakingapp.api;

import android.util.Log;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiManager {
    private String TAG = ApiManager.class.getSimpleName();
    private static volatile ApiManager sharedInstance = new ApiManager();
    private RecipeApiService RecipeApiService;

    private ApiManager() {
        if (sharedInstance != null) {
            throw new RuntimeException("You can only make one instance of this class.");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.REQUEST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeApiService = retrofit.create(RecipeApiService.class);
    }

    public static ApiManager getInstance() {
        if (sharedInstance == null) {
            synchronized (ApiManager.class) {
                if (sharedInstance == null) sharedInstance = new ApiManager();
            }
        }
        return sharedInstance;
    }


    public void getRecipes(final ApiCallback<List<Recipe>> recipesApiCallback) {
        RecipeApiService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipesApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable throwable) {
                if (call.isCanceled()) {
                    Log.e(TAG, "Request was cancelled");
                    recipesApiCallback.onCancel();
                } else {
                    Log.e(TAG, throwable.getMessage());
                    recipesApiCallback.onResponse(null);
                }
            }
        });
    }
}
