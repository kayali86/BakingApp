package com.kayali_developer.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.bakingapp.data.model.Recipe;
import com.kayali_developer.bakingapp.databinding.RecipeListItemBinding;

import java.util.List;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeAdapterViewHolder> {
    private List<Recipe> recipes;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public RecipesListAdapter(RecipeAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        RecipeListItemBinding itemBinding = RecipeListItemBinding.inflate(inflater, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder recipeAdapterViewHolder, int i) {
        Recipe currentRecipe = recipes.get(i);
        recipeAdapterViewHolder.bind(currentRecipe);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecipeListItemBinding itemBinding;

        RecipeAdapterViewHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            String recipeId = String.valueOf(recipe.getId());
            String recipeServings = String.valueOf(recipe.getServings());
            itemBinding.tvRecipeName.setText(recipe.getName());
            itemBinding.tvRecipeId.setText(recipeId);
            itemBinding.tvRecipeServings.setText(recipeServings);
            itemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe currentRecipe = recipes.get(adapterPosition);
            mClickHandler.onClick(currentRecipe);
        }
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe currentRecipe);
    }

    public void setRecipesData(List<Recipe> recipesData) {
        recipes = recipesData;
        notifyDataSetChanged();
    }
}
