package jonathan.mason.baking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import jonathan.mason.baking_app.Data.Recipe;

import android.os.Bundle;

/**
 * Screen for displaying information about recipe selected from MainActivity screen.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeStepsAdapter.RecipeStepSelectionListener {
    private Recipe mRecipe;
    private boolean mTwoPane;
    private Fragment mSecondPaneFragment;

    /**
     * Perform initialisation, retrieving recipe passed from MainActivity screen and setting
     * up ingredients, recipe steps and optionally details of current step, if device is big
     * enough.
     * @param savedInstanceState Saved state of activity; not used.
     * @exception RuntimeException Thrown if intent does not contain Recipe.SELECTED_RECIPE
     * key within extra data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(this.getIntent().hasExtra(Recipe.SELECTED_RECIPE) == false)
            throw new RuntimeException("No recipe passed to " + this.toString());

        // Retrieve selected recipe passed through intent by MainActivity screen.
        mRecipe = this.getIntent().getParcelableExtra(Recipe.SELECTED_RECIPE);

        // Set title of recipe.
        getSupportActionBar().setTitle(this.getString(R.string.recipe_title, mRecipe.getName(), mRecipe.getServings())); // Set action bar title with name of recipe and servings.

        // Attach recipe fragment, containing recipe steps, to its container, to
        // left of activity.
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if(savedInstanceState == null) {
            RecipeFragment recipeFragment = new RecipeFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_recipe_container, recipeFragment).commit();
        }

        // Workout if layout used is two pane version for tablets.
        mTwoPane = false;
        mSecondPaneFragment = null;
        if(this.findViewById(R.id.fragment_ingredients_or_steps_container) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                // Attach ingredients fragment to its container to right of activity.
                mSecondPaneFragment = new IngredientsFragment();
                fragmentManager.beginTransaction().add(R.id.fragment_ingredients_or_steps_container, mSecondPaneFragment).commit();
            }
            else {
                // Retrieve fragment to right of activity, which could be for ingredients
                // or recipe step details.
                mSecondPaneFragment = fragmentManager.findFragmentById(R.id.fragment_ingredients_or_steps_container);
            }
        }
    }

    /******************************************
     * Implement RecipeStepSelectionListener. *
     ******************************************/

    /**
     * Handle selection of recipe step to show its details.
     * @param selectedRecipeStepIndex Selected recipe step index.
     */
    public void onRecipeStepSelected(int selectedRecipeStepIndex)
    {
        if(mTwoPane) {
            // Ensure recipe step details fragment is displayed.
            if (mSecondPaneFragment instanceof IngredientsFragment) {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                mSecondPaneFragment = new RecipeStepDetailsFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_ingredients_or_steps_container, mSecondPaneFragment).commitNow();
            }

            // Set selected step.
            ((RecipeStepDetailsFragment)mSecondPaneFragment).updateRecipeStep(selectedRecipeStepIndex);
        }
        else {

            // Show separate RecipeStepDetailsActivity screen, which has same fragment.
            Intent intent = new Intent(this, RecipeStepDetailsActivity.class);
            intent.putExtra(Recipe.SELECTED_RECIPE, mRecipe); // Pass selected recipe as extra data in intent.
            intent.putExtra(Recipe.CURRENT_STEP_INDEX, selectedRecipeStepIndex); // Pass index of selected recipe step as extra data in intent.
            this.startActivity(intent);
        }
    }

    /**
     * Handle selection of ingredients.
     */
    @Override
    public void onIngredientsClicked() {
        if(mTwoPane) {
            // Ensure recipe step details fragment is displayed.
            if (mSecondPaneFragment instanceof RecipeStepDetailsFragment) {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                mSecondPaneFragment = new IngredientsFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_ingredients_or_steps_container, mSecondPaneFragment).commit();
            }
        }
        else {
            // Show separate IngredientsActivity screen, which has same fragment.
            Intent intent = new Intent(this, IngredientsActivity.class);
            intent.putExtra(Recipe.SELECTED_RECIPE, mRecipe); // Pass selected recipe as extra data in intent.
            this.startActivity(intent);
        }
    }
}
