package com.kayali_developer.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.bakingapp.R;
import com.kayali_developer.bakingapp.data.model.Step;
import com.kayali_developer.bakingapp.databinding.StepListItemBinding;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {
    private List<Step> steps;
    private final StepAdapterOnClickHandler mClickHandler;
    // Detect the selected item to make selector - default value is -1
    private int clickedItemPosition = -1;

    public StepAdapter(StepAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        StepListItemBinding itemBinding = StepListItemBinding.inflate(inflater, viewGroup, shouldAttachToParentImmediately);
        return new StepAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder stepAdapterViewHolder, int position) {
        stepAdapterViewHolder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private StepListItemBinding stepBinding;

        StepAdapterViewHolder(StepListItemBinding binding) {
            super(binding.getRoot());
            stepBinding = binding;
            itemView.setOnClickListener(this);
        }

        void bindItem(int position) {
            Step currentStep = steps.get(position);
            String stepId = String.valueOf(currentStep.getId() + 1);
            stepBinding.tvStepId.setText(stepId);
            stepBinding.tvStepShortDescription.setText(currentStep.getShortDescription());
            // Detect the selected item to make selector
            if (clickedItemPosition == position) {
                stepBinding.stepItemContainer.setBackgroundResource(R.drawable.recipe_background_dark);
            } else {
                stepBinding.stepItemContainer.setBackgroundResource(R.drawable.recipe_background);
            }
            stepBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            clickedItemPosition = adapterPosition;
            mClickHandler.onClick(adapterPosition);
            notifyDataSetChanged();
        }
    }

    // An interface to pass clicked item position to RecipeFragment
    public interface StepAdapterOnClickHandler {
        void onClick(int position);
    }

    // Helper method to set data to adapter
    public void setStepsData(List<Step> stepsData) {
        steps = stepsData;
        notifyDataSetChanged();
    }
}
