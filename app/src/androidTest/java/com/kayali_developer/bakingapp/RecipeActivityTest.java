package com.kayali_developer.bakingapp;

import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kayali_developer.bakingapp.activity.MainActivity;
import com.kayali_developer.bakingapp.activity.RecipeActivity;
import com.kayali_developer.bakingapp.fragment.RecipeFragment;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.data.model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeActivityTest {

    private static final int CLICKED_ITEM_IN_ACTIVITY = 0;
    private static final int ITEM_BELOW_THE_FOLD = 2;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Rule
    public ActivityTestRule<RecipeActivity> mRecipeActivityRule = new ActivityTestRule<>(
            RecipeActivity.class);


    @Before
    public void setup() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(CLICKED_ITEM_IN_ACTIVITY, click()));

        // Match the text in an item below the fold and check that it's displayed.
        String itemElementText = "Nutella Pie";
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        List<Step> steps = new ArrayList<>();
        Step step0 = new Step(0, "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", "");

        Step step1 = new Step(1, "Starting prep", "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
                "", "");

        Step step2 = new Step(2, "Prep the cookie crust.", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4", "");

        steps.add(step0);
        steps.add(step1);
        steps.add(step2);

        Recipe currentRecipe = new Recipe(1, "Nutella Pie", null, steps, 8, "");

        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle recipeBundle = new Bundle();
        recipeBundle.putParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY, currentRecipe);
        recipeFragment.setArguments(recipeBundle);
        mRecipeActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.recipe_fragment_container, recipeFragment).commit();


        onView(withId(R.id.steps_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, click()));

        String itemElementText = "Prep the cookie crust.";
        onView(withText(itemElementText)).check(matches(isDisplayed()));

    }

}
