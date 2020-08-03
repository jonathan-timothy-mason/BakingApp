package jonathan.mason.baking_app;

import java.util.ArrayList;

import jonathan.mason.baking_app.Data.Recipe;
import jonathan.mason.baking_app.Data.RecipeIngredient;
import jonathan.mason.baking_app.Data.RecipeStep;

/**
 * Functions to help with testing.
 */
public class TestUtils {
    /**
     * Create a test recipe.
     * @return Test recipe.
     */
    public static Recipe getTestRecipe() {

        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(new RecipeIngredient(100, "g", "Flour"));
        ingredients.add(new RecipeIngredient(200, "g", "Cheese"));
        ingredients.add(new RecipeIngredient(300, "g", "Tomatoes"));

        ArrayList<RecipeStep> steps = new ArrayList<>();
        steps.add(new RecipeStep(1, "Make bread", "Mix the flour with a tablespoon of water, a pinch of salt and yeast.", "", ""));
        steps.add(new RecipeStep(2, "Apply tomato", "Spread tomatoes over bread once it has risen.", "", ""));
        steps.add(new RecipeStep(3, "Top with cheese", "Liberally grate cheese over the top and bake for 15 minutes.", "", ""));

        return new Recipe(1, "Pizza", 4, "Pizza.png", ingredients, steps);
    }
}
