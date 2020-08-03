package jonathan.mason.baking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import jonathan.mason.baking_app.Data.Recipe;

/**
 * Screen for displaying ingredients of recipe selected from RecipeActivity screen.
 */
public class IngredientsActivity extends AppCompatActivity {
    private Recipe mRecipe;
    private IngredientsFragment mIngredientsFragment;

    /**
     * Perform initialisation, retrieving recipe passed from RecipeActivity screen and setting
     * its ingredients.
     * @param savedInstanceState Saved state of activity; not used.
     * @exception RuntimeException Thrown if intent does not contain Recipe.SELECTED_RECIPE
     * key within extra data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        if(this.getIntent().hasExtra(Recipe.SELECTED_RECIPE) == false)
            throw new RuntimeException("No recipe passed to " + this.toString());

        // Retrieve selected recipe passed through intent by RecipeActivity screen.
        mRecipe = this.getIntent().getParcelableExtra(Recipe.SELECTED_RECIPE);

        // Set title of recipe.
        getSupportActionBar().setTitle(this.getString(R.string.recipe_title, mRecipe.getName(), mRecipe.getServings())); // Set action bar title with name of recipe and servings.

        // Create or retrieve ingredients fragment.
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if(savedInstanceState == null) {
            mIngredientsFragment = new IngredientsFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_ingredients_container, mIngredientsFragment).commit();
        }
        else {
            mIngredientsFragment = (IngredientsFragment)fragmentManager.findFragmentById(R.id.fragment_ingredients_container);
        }
    }
}
