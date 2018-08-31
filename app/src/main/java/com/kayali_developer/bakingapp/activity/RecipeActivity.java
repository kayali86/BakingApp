package com.kayali_developer.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.fragment.IngredientsFragment;
import com.kayali_developer.bakingapp.fragment.RecipeFragment;
import com.kayali_developer.bakingapp.fragment.StepFragment;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnCurrentRecipeClickListener {
    private Recipe currentRecipe = new Recipe();
    // TwoPane = Tablet device
    // The IngredientsFragment and StepFragment will be added in RecipeActivity instead of RecipeDetailActivity
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        // detail_fragment_container is only in sw600dp layout
        if (findViewById(R.id.detail_fragment_container) != null) {
            // Tablet
            mTwoPane = true;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    currentRecipe = extras.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    RecipeFragment recipeFragment = new RecipeFragment();
                    Bundle recipeBundle = new Bundle();
                    recipeBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
                    recipeFragment.setArguments(recipeBundle);
                    fragmentManager.beginTransaction().add(R.id.recipe_fragment_container, recipeFragment)
                            .commit();

                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    Bundle ingredientsBundle = new Bundle();
                    ingredientsBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
                    ingredientsFragment.setArguments(ingredientsBundle);
                    fragmentManager.beginTransaction().add(R.id.detail_fragment_container, ingredientsFragment)
                            .commit();
                }
            } else {
                currentRecipe = savedInstanceState.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
            }
            if (currentRecipe != null) setTitle(currentRecipe.getName());

        } else {
            // Smartphone
            mTwoPane = false;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    currentRecipe = extras.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    RecipeFragment recipeFragment = new RecipeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
                    recipeFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.recipe_fragment_container, recipeFragment)
                            .commit();
                }
            } else {
                currentRecipe = savedInstanceState.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
            }
            if (currentRecipe != null) setTitle(currentRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepSelected(int position) {
        // Tablet
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepFragment stepFragment = new StepFragment();
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
            stepBundle.putInt(AppConstants.CURRENT_STEP_POSITION_KEY, position);
            stepBundle.putBoolean(AppConstants.TWO_PANE_KEY, true);
            stepFragment.setArguments(stepBundle);
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, stepFragment)
                    .commit();
        } else {
            // Smartphone
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
            intent.putExtra(AppConstants.CURRENT_STEP_POSITION_KEY, position);
            intent.setAction(AppConstants.RECIPE_ACTION_STEP);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            Log.w(RecipeActivity.class.getSimpleName(), ">>>>>>>>>>>>>>>>>>>>>>>>>onStepSelected Activity");
        }
    }

    @Override
    public void onIngredientsSelected() {
        // Tablet
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            IngredientsFragment ingredientFragment = new IngredientsFragment();
            Bundle ingredientsBundle = new Bundle();
            ingredientsBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
            ingredientFragment.setArguments(ingredientsBundle);
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, ingredientFragment)
                    .commit();
        } else {
            // Smartphone
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
            intent.setAction(AppConstants.RECIPE_ACTION_INGREDIENTS);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
