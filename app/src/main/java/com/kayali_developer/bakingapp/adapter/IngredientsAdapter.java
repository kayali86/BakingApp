package com.kayali_developer.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kayali_developer.bakingapp.data.model.Ingredient;
import com.kayali_developer.bakingapp.databinding.IngredientListItemBinding;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientAdapterViewHolder> {
    private List<Ingredient> ingredients;

    public IngredientsAdapter() {
    }

    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;
        IngredientListItemBinding itemBinding = IngredientListItemBinding.inflate(inflater, viewGroup, shouldAttachToParentImmediately);
        return new IngredientAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder ingredientAdapterViewHolder, int i) {
        Ingredient currentIngredient = ingredients.get(i);
        ingredientAdapterViewHolder.bindItem(currentIngredient);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {
        private IngredientListItemBinding ingredientBinding;

        IngredientAdapterViewHolder(IngredientListItemBinding binding) {
            super(binding.getRoot());
            ingredientBinding = binding;
        }

        void bindItem(Ingredient currentIngredient) {
            String ingredientQuantity = String.valueOf(currentIngredient.getQuantity());
            ingredientBinding.tvIngredient.setText(currentIngredient.getIngredient());
            ingredientBinding.tvQuantity.setText(ingredientQuantity);
            ingredientBinding.tvMeasure.setText(currentIngredient.getMeasure());
            ingredientBinding.executePendingBindings();
        }
    }

    public void setIngredientsData(List<Ingredient> ingredientsData) {
        ingredients = ingredientsData;
        notifyDataSetChanged();
    }
}
