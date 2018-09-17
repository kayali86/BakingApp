package com.kayali_developer.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String recipesStr = intent.getStringExtra(AppConstants.ALL_RECIPES_KEY_WIDGET);
        return new IngredientsListWidgetRemoteViewsFactory(this.getApplicationContext(), recipesStr);
    }
}
