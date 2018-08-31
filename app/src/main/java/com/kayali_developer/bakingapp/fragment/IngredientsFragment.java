package com.kayali_developer.bakingapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.bakingapp.AppConstants;
import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.adapter.IngredientsAdapter;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.databinding.FragmentIngredientsBinding;

public class IngredientsFragment extends Fragment {
    private Recipe currentRecipe = new Recipe();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentIngredientsBinding ingredientsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);
        View view = ingredientsBinding.getRoot();
        currentRecipe = getArguments().getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
        if (currentRecipe != null) {
            LinearLayoutManager ingredientLayoutManager
                    = new LinearLayoutManager(getActivity());
            ingredientsBinding.ingredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
            ingredientsBinding.ingredientsRecyclerView.setHasFixedSize(true);
            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
            ingredientsBinding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);
            ingredientsAdapter.setIngredientsData(currentRecipe.getIngredients());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentRecipe != null) getActivity().setTitle(currentRecipe.getName());
    }
}
