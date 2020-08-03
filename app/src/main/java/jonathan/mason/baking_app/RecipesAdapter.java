package jonathan.mason.baking_app;

import butterknife.BindView;
import butterknife.ButterKnife;
import jonathan.mason.baking_app.Data.Recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * RecyclerView adapter for providing recipes.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    /**
     * Listener for notification of recipe selection.
     */
    public interface RecipeSelectionListener {
        /**
         * Handle selection of recipe.
         * @param selectedRecipe Selected recipe.
         */
        void onRecipeSelected(Recipe selectedRecipe);
    }

    private int mWidth;
    private int mHeight;
    private List<Recipe> mRecipes;
    private RecipeSelectionListener mRecipeSelectionListener;

    /**
     * Constructor.
     * @param width Width to which view is to be sized.
     * @param height Height to which view is to be sized.
     * @param recipes Recipes to be supplied to RecyclerView.
     * @param recipeSelectionListener Listener for notification of recipe selection.
     */
    public RecipesAdapter(int width, int height, List<Recipe> recipes, RecipeSelectionListener recipeSelectionListener)
    {
        mWidth = width;
        mHeight = height;
        mRecipes = recipes;
        mRecipeSelectionListener = recipeSelectionListener;
    }

    /**
     * Create instance of RecipeViewHolder class.
     * @param parent Parent ViewGroup to which view holder is to be added.
     * @param viewType Unused item type.
     * @return Instance of RecipeViewHolder class.
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dynamically create layout for item.
        View recipeItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);

        // Create ViewHolder.
        return new RecipeViewHolder(recipeItem);
    }

    /**
     * Bind supplied view holder to recipe at specified position in recipes list.
     * @param holder View holder to bind.
     * @param position Position of recipe.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.Bind(position);
    }

    /**
     * Get total number of recipes.
     * @return Total number of recipes.
     */
    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    /**
     * A ViewHolder subclass suitable for displaying a recipe item.
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        @BindView(R.id.recipe_image) ImageView mRecipeImage;
        @BindView(R.id.recipe_name) TextView mRecipeName;

        /**
         * Constructor.
         * @param itemView View corresponding to recipe item, to be bound to view holder.
         */
        public RecipeViewHolder(View itemView) {
            super(itemView);

            // Retrieve view(s).
            ButterKnife.bind(this, itemView);

            // Size image, which determines size of whole item, which is set to wrap image.
            mRecipeImage.setMinimumHeight(mHeight);
            mRecipeImage.setMinimumWidth(mWidth);

            // Setup click listener.
            itemView.setOnClickListener(this);
        }

        /**
         * Bind view holder to recipe at specified position in recipes list.
         * @param position Position of recipe.
         */
        public void Bind(int position){
            Recipe recipe = mRecipes.get(position);
            if((recipe.getImage() != null) && !recipe.getImage().isEmpty()) {
                // Load specified image without caption.
                Picasso.get().load(recipe.getImage()).into(mRecipeImage);
                mRecipeName.setText("");
            }
            else {
                // Reset to default image with caption.
                mRecipeImage.setImageDrawable(mRecipeImage.getResources().getDrawable(R.drawable.cake));
                mRecipeName.setText(recipe.getName());
            }
        }

        /**
         * Handle selection of view holder to notify recipe selection listener of adapter.
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mRecipeSelectionListener.onRecipeSelected(mRecipes.get(position));
        }
    }
}
