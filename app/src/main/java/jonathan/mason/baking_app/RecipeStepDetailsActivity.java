package jonathan.mason.baking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;

import jonathan.mason.baking_app.Data.Recipe;

import android.os.Bundle;
import android.view.MenuItem;

/**
 * Screen for displaying details about recipe step selected from RecipeActivity screen.
 */
public class RecipeStepDetailsActivity extends AppCompatActivity {
    private Recipe mRecipe;
    private RecipeStepDetailsFragment mRecipeStepDetailsFragment;

    /**
     * Perform initialisation, retrieving recipe passed from RecipeActivity screen and setting
     * details of current step.
     * @param savedInstanceState Saved state of activity; not used.
     * @exception RuntimeException thrown if intent does not contain Recipe.SELECTED_RECIPE
     * key within extra data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(this.getIntent().hasExtra(Recipe.SELECTED_RECIPE) == false)
            throw new RuntimeException("No recipe passed to " + this.toString());

        // Retrieve selected recipe passed through intent by RecipeActivity screen.
        mRecipe = this.getIntent().getParcelableExtra(Recipe.SELECTED_RECIPE);

        // Set title of recipe.
        getSupportActionBar().setTitle(this.getString(R.string.recipe_title, mRecipe.getName(), mRecipe.getServings())); // Set action bar title with name of recipe and servings.

        // Create or retrieve recipe step details fragment (containing video and details
        // of step).
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if(savedInstanceState == null) {
            mRecipeStepDetailsFragment = new RecipeStepDetailsFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_steps_container, mRecipeStepDetailsFragment).commit();
        }
        else {
            mRecipeStepDetailsFragment = (RecipeStepDetailsFragment)fragmentManager.findFragmentById(R.id.fragment_steps_container);
        }
    }
}
