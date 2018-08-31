package com.kayali_developer.bakingapp;

public class AppConstants {
    // Request url
    public static final String REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    // Bundle Keys
    public static final String CURRENT_RECIPE_PARCELABLE_KEY = "current_recipe";
    public static final String CURRENT_STEP_POSITION_KEY = "current_step";
    public static final String TWO_PANE_KEY = "two_pane";
    public static final String CURRENT_RECIPE_POSITION_KEY_WIDGET = "current_recipe_position_widget";
    public static final String ALL_RECIPES_KEY_WIDGET = "all_recipes_widget";
    // Intent Actions
    public static final String RECIPE_ACTION_INGREDIENTS = "call_ingredients";
    public static final String RECIPE_ACTION_STEP = "call_step";
    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "startActionUpdateRecipeWidgets";
    public static final String ACTION_PREVIOUS_RECIPE = "startActionPreviousRecipe";
    public static final String ACTION_NEXT_RECIPE = "startActionNextRecipe";
    public static final String ACTION_GET_RECIPES = "get_recipes";
    // Notification Channel Id
    public static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    // Recipes Grid layout span count in MainActivity
    public static final int GRID_LAYOUT_SPAN_COUNT = 3;
}
