package com.kayali_developer.bakingapp.fragment;

import android.content.Context;
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
import com.kayali_developer.bakingapp.adapter.StepAdapter;
import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.databinding.FragmentRecipeBinding;

public class RecipeFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {
    private OnCurrentRecipeClickListener mCallback;

    @Override
    public void onClick(int position) {
        mCallback.onStepSelected(position);
    }

    public interface OnCurrentRecipeClickListener {
        void onStepSelected(int position);

        void onIngredientsSelected();
    }

    // To be sure that OnCurrentRecipeClickListener is implemented in parent activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnCurrentRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCurrentRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRecipeBinding recipeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe, container, false);
        View view = recipeBinding.getRoot();
        LinearLayoutManager stepLayoutManager
                = new LinearLayoutManager(getActivity());
        recipeBinding.stepsRecyclerView.setLayoutManager(stepLayoutManager);
        recipeBinding.stepsRecyclerView.setHasFixedSize(true);
        StepAdapter stepAdapter = new StepAdapter(this);
        recipeBinding.stepsRecyclerView.setAdapter(stepAdapter);
        Bundle bundle = getArguments();
        Recipe currentRecipe = bundle.getParcelable(AppConstants.CURRENT_RECIPE_PARCELABLE_KEY);
        if (currentRecipe != null) {
            stepAdapter.setStepsData(currentRecipe.getSteps());
            recipeBinding.tvRecipeHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onIngredientsSelected();
                }
            });
        }
        return view;
    }
}
