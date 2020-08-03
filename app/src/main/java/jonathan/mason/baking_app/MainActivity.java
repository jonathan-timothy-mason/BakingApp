package jonathan.mason.baking_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import jonathan.mason.baking_app.Data.Recipe;
import jonathan.mason.baking_app.Data.RecipesViewModel;

import android.graphics.Rect;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main screen of app, displaying recipes.
 */
public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeSelectionListener {

    @BindView(R.id.recipes_recycler_view) RecyclerView mRecipesRecyclerView;

    /**
     * Perform initialisation of recipes and RecyclerView with creation of activity.
     * @param savedInstanceState Saved state of activity; not used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve view(s).
        ButterKnife.bind(this);

        // Set up to use GridLayoutManager (recipes adapter created after recipes
        // have loaded).
        mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(this, this.getNumberColumns()));

        // Setup up ViewModel to load and cache recipes.
        this.setupViewModel();
    }

    /**
     * Determine number of columns to use for RecyclerView depending upon whether screen width.
     * @return Number of columns to display in RecyclerView.
     */
    private int getNumberColumns() {
        if(Utils.isTablet(this)) {
            if(Utils.isLandscape(this))
                return 3;
            else
                return 2;
        }
        else {
            if(Utils.isLandscape(this))
                return 2;
            else
                return 1;
        }
    }

    /**
     * Setup up ViewModel to load and cache recipes on separate thread for lifetime of activity.
     */
    private void setupViewModel() {
        RecipesViewModel viewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

                // If there are no recipes tell user, but continue so that RecyclerView is
                // cleared to reflect that their are no recipes.
                if (recipes.size() <= 0)
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.err_no_recipes), Toast.LENGTH_SHORT).show();

                // Update RecyclerView.
                MainActivity.this.setRecyclerViewAdapter(recipes);
            }
        });
    }

    /**************************************
     * Implement RecipeSelectionListener. *
     **************************************/

    /**
     * Handle selection of recipe to show RecipeActivity screen.
     * @param selectedRecipe Selected recipe.
     */
    public void onRecipeSelected(Recipe selectedRecipe)
    {
        // Show RecipeActivity screen.
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(Recipe.SELECTED_RECIPE, selectedRecipe); // Pass selected recipe as extra data in intent.
        startActivity(intent);

        // Also update any widgets to show last selected ingredients.
        BakingAppWidget.updateAllAppWidgets(this, AppWidgetManager.getInstance(this), selectedRecipe);
    }

    /**
     * Add recipes to adapter and then to RecyclerView.
     * @param recipes Recipes to add.
     */
    private void setRecyclerViewAdapter(List<Recipe> recipes) {
        // Determine size to which cells of grid should be sized.
        Rect sizeOfScreen = new Rect();
        getWindowManager().getDefaultDisplay().getRectSize(sizeOfScreen);
        int numColumns = ((GridLayoutManager)mRecipesRecyclerView.getLayoutManager()).getSpanCount();
        int width = sizeOfScreen.width() / numColumns;
        int height = (int)(width * 0.67); // Set aspect ratio of 3 x 2.

        // Create recipes adapter (set after recipes loaded to begin displaying).
        RecipesAdapter recipesAdapter = new RecipesAdapter(width, height, recipes,this);

        // Set adapter of RecycleView (this causes it to update itself).
        mRecipesRecyclerView.setAdapter(recipesAdapter);
    }
}
