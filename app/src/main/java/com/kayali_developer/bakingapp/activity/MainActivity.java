package com.kayali_developer.bakingapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.adapter.RecipesListAdapter;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.databinding.ActivityMainBinding;
import com.kayali_developer.bakingapp.idlingresource.RecipesIdlingResource;
import com.kayali_developer.bakingapp.utilities.RecipeNetworkUtils;
import com.kayali_developer.bakingapp.view_model.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipesListAdapter.RecipeAdapterOnClickHandler {
    private ActivityMainBinding mainBinding;
    private RecipesListAdapter recipesListAdapter;

    @Nullable
    private RecipesIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipesIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        RecyclerView.LayoutManager layoutManager;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            layoutManager = new GridLayoutManager(this, AppConstants.GRID_LAYOUT_SPAN_COUNT);
        } else {
            // In portrait
            layoutManager = new LinearLayoutManager(this);
        }
        mainBinding.recipesRecyclerView.setLayoutManager(layoutManager);
        mainBinding.recipesRecyclerView.setHasFixedSize(true);
        recipesListAdapter = new RecipesListAdapter(this);
        mainBinding.recipesRecyclerView.setAdapter(recipesListAdapter);
        setupViewModel();
        getIdlingResource();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (RecipeNetworkUtils.isConnected(this) && viewModel.getRecipes() != null) {
            showRecipesDataView();
            viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
                @Override
                public void onChanged(@Nullable List<Recipe> recipes) {
                    recipesListAdapter.setRecipesData(recipes);
                    recipesListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            showErrorMessage();
        }
    }

    // Helper Method to hide emptyView and show recyclerView to display retrieved data
    private void showRecipesDataView() {
        mainBinding.emptyView.setVisibility(View.INVISIBLE);
        mainBinding.recipesRecyclerView.setVisibility(View.VISIBLE);
    }

    // Helper Method to hide recyclerView and show emptyView
    private void showErrorMessage() {
        mainBinding.recipesRecyclerView.setVisibility(View.INVISIBLE);
        mainBinding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe currentRecipe) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
