package com.kayali_developer.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.fragment.IngredientsFragment;
import com.kayali_developer.bakingapp.fragment.StepFragment;

public class RecipeDetailActivity extends AppCompatActivity implements StepFragment.NextPreviousClickListener {
    private Recipe currentRecipe;
    private int stepPosition;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                currentRecipe = extras.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
                stepPosition = extras.getInt(AppConstants.CURRENT_STEP_POSITION_KEY);
                // Get action to detect if ingredients is clicked
                if (intent.getAction().equals(AppConstants.RECIPE_ACTION_INGREDIENTS)) {
                    setTitle(currentRecipe.getName());
                    IngredientsFragment ingredientFragment = new IngredientsFragment();
                    Bundle ingredientsBundle = new Bundle();
                    ingredientsBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
                    ingredientFragment.setArguments(ingredientsBundle);
                    fragmentManager.beginTransaction().add(R.id.ingredients_fragment_container, ingredientFragment)
                            .commit();
                } else {
                    // Get action to detect if a step is clicked
                    setTitle(currentRecipe.getSteps().get(stepPosition).getShortDescription());
                    StepFragment stepFragment = new StepFragment();
                    Bundle stepBundle = new Bundle();
                    stepBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
                    stepBundle.putInt(AppConstants.CURRENT_STEP_POSITION_KEY, stepPosition);
                    stepBundle.putBoolean(AppConstants.TWO_PANE_KEY, false);
                    stepFragment.setArguments(stepBundle);
                    fragmentManager.beginTransaction().add(R.id.step_fragment_container, stepFragment)
                            .commit();
                }
            }
        } else {
            currentRecipe = savedInstanceState.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
            stepPosition = savedInstanceState.getInt(AppConstants.CURRENT_STEP_POSITION_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        outState.putInt(AppConstants.CURRENT_STEP_POSITION_KEY, stepPosition);
        super.onSaveInstanceState(outState);
    }

    // Implement NextPreviousClickListener interface in StepFragment to handle nextStep function
    @Override
    public void onNextClicked() {
        setTitle(currentRecipe.getSteps().get(stepPosition).getShortDescription());
        int stepsCount = currentRecipe.getSteps().size();
        if (stepPosition < stepsCount - 1) {
            stepPosition++;
            replaceStepFragment();
        } else {
            Toast.makeText(this, getString(R.string.last_step_toast_message), Toast.LENGTH_SHORT).show();
        }
    }

    // Implement NextPreviousClickListener interface in StepFragment to handle previousStep function
    @Override
    public void onPreviousClicked() {
        setTitle(currentRecipe.getSteps().get(stepPosition).getShortDescription());
        if (stepPosition > 0) {
            stepPosition--;
            replaceStepFragment();
        } else {
            Toast.makeText(this, getString(R.string.first_step_toast_message), Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to replace current fragment by next or previous stepFragment
    private void replaceStepFragment() {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        bundle.putInt(AppConstants.CURRENT_STEP_POSITION_KEY, stepPosition);
        bundle.putBoolean(AppConstants.TWO_PANE_KEY, false);
        stepFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_fragment_container, stepFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_detail_menu, menu);
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
