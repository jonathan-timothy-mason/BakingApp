package jonathan.mason.baking_app;

import butterknife.BindView;
import butterknife.ButterKnife;
import jonathan.mason.baking_app.Data.RecipeStep;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView adapter for providing recipe steps.
 */
public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepViewHolder> {

    /**
     * Listener for notification of recipe step selection.
     */
    public interface RecipeStepSelectionListener {
        /**
         * Handle selection of recipe step.
         * @param selectedRecipeStepIndex Selected recipe step index.
         */
        void onRecipeStepSelected(int selectedRecipeStepIndex);

        /**
         * Handle selection of ingredients.
         */
        void onIngredientsClicked();
    }

    private List<RecipeStep> mRecipeSteps;
    private RecipeStepSelectionListener mRecipeStepSelectionListener;

    /**
     * Constructor.
     * @param recipeSteps Recipe steps to be supplied to RecyclerView.
     * @param recipeStepSelectionListener Listener for notification of recipe step selection.
     */
    public RecipeStepsAdapter(List<RecipeStep> recipeSteps, RecipeStepSelectionListener recipeStepSelectionListener)
    {
        mRecipeSteps = recipeSteps;
        mRecipeStepSelectionListener = recipeStepSelectionListener;
    }

    /**
     * Create instance of RecipeViewHolder class.
     * @param parent Parent ViewGroup to which view holder is to be added.
     * @param viewType Unused item type.
     * @return Instance of RecipeStepViewHolder class.
     */
    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dynamically create layout for item.
        View recipeStepItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item, parent, false);

        // Create ViewHolder.
        return new RecipeStepViewHolder(recipeStepItem);
    }

    /**
     * Bind supplied view holder to recipe step at specified position in recipe steps list.
     * @param holder View holder to bind.
     * @param position Position of recipe step.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        holder.Bind(position);
    }

    /**
     * Get total number of recipe steps.
     * @return Total number of recipe steps.
     */
    @Override
    public int getItemCount() {
        return mRecipeSteps.size() + 1 ; // Add one for ingredients.
    }

    /**
     * A ViewHolder subclass suitable for displaying a recipe step item.
     */
    public class RecipeStepViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        @BindView(R.id.recipe_step_caption) TextView mRecipeStepCaption;

        /**
         * Constructor.
         * @param itemView View corresponding to recipe step item, to be bound to view holder.
         */
        public RecipeStepViewHolder(View itemView) {
            super(itemView);

            // Retrieve view(s).
            ButterKnife.bind(this, itemView);

            // Setup click listener.
            itemView.setOnClickListener(this);
        }

        /**
         * Bind view holder to recipe step at specified position in recipe steps list.
         * @param position Position of recipe step.
         */
        public void Bind(int position){
            if(position == 0) {
                mRecipeStepCaption.setText(mRecipeStepCaption.getResources().getString(R.string.step_bullet) + " " + mRecipeStepCaption.getResources().getString(R.string.ingredients_title));
            }
            else {
                RecipeStep recipeStep = mRecipeSteps.get(position - 1); // Remove one for ingredients.
                mRecipeStepCaption.setText(mRecipeStepCaption.getResources().getString(R.string.step_bullet) + " " + recipeStep.getShortDescription());
            }
        }

        /**
         * Handle selection of view holder to notify recipe step selection listener of adapter.
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position == 0)
                mRecipeStepSelectionListener.onIngredientsClicked();
            else
                mRecipeStepSelectionListener.onRecipeStepSelected(position - 1); // Remove one for ingredients.
        }
    }
}