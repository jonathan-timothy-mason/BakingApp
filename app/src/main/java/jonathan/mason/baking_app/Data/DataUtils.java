package jonathan.mason.baking_app.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;

/**
 * Data-related utility functions.
 */
public class DataUtils {
    // Recipes URLs.
    private final static String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Create URL to load recipes.
     * @return The created URL or null if there was a problem.
     */
    public static URL createRecipesURL()
    {
        try {
            return new URL(RECIPES_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Recipes JSON data names.
    private final static String RECIPE_ID = "id";
    private final static String RECIPE_NAME = "name";
    private final static String RECIPE_SERVINGS = "servings";
    private final static String RECIPE_IMAGE = "image";
    private final static String RECIPE_INGREDIENTS = "ingredients";
    private final static String RECIPE_INGREDIENT_QUANTITY = "quantity";
    private final static String RECIPE_INGREDIENT_MEASURE = "measure";
    private final static String RECIPE_INGREDIENT_INGREDIENT = "ingredient";
    private final static String RECIPE_STEPS = "steps";
    private final static String RECIPE_STEP_ID = "id";
    private final static String RECIPE_STEP_SHORT_DESCRIPTION = "shortDescription";
    private final static String RECIPE_STEP_DESCRIPTION = "description";
    private final static String RECIPE_STEP_VIDEO_URL = "videoURL";
    private final static String RECIPE_STEP_THUMBNAIL_URL = "thumbnailURL";

    /**
     * Convert supplied recipes JSON string to list of recipes.
     * @param recipesAsJSONString Recipes JSON string to convert.
     * @return Created recipes list.
     */
    public static ArrayList<Recipe> parseRecipesJSON(String recipesAsJSONString) {
        ArrayList<Recipe> recipesAsList = new ArrayList<>();

        try {
            // Root data is simply a JSON array, so convert to that.
            JSONArray recipesAsJSONArray = new JSONArray(recipesAsJSONString);
            for (int recipeIndex = 0; recipeIndex < recipesAsJSONArray.length(); recipeIndex++) {
                // Get recipe within array.
                JSONObject recipeAsJSONObject = recipesAsJSONArray.getJSONObject(recipeIndex);

                // Extract simple field information.
                int recipeID = recipeAsJSONObject.getInt(RECIPE_ID);
                String name = recipeAsJSONObject.getString(RECIPE_NAME);
                int servings = recipeAsJSONObject.getInt(RECIPE_SERVINGS);
                String image = recipeAsJSONObject.getString(RECIPE_IMAGE);

                // Extract ingredients array.
                ArrayList<RecipeIngredient> ingredientsAsList = new ArrayList<>();
                JSONArray ingredientsAsJSONArray = recipeAsJSONObject.getJSONArray(RECIPE_INGREDIENTS);
                for (int ingredientsIndex = 0; ingredientsIndex < ingredientsAsJSONArray.length(); ingredientsIndex++) {
                    // Get ingredient within array.
                    JSONObject ingredientAsJSONObject = ingredientsAsJSONArray.getJSONObject(ingredientsIndex);

                    // Extract simple field information.
                    int quantity = ingredientAsJSONObject.getInt(RECIPE_INGREDIENT_QUANTITY);
                    String measure = ingredientAsJSONObject.getString(RECIPE_INGREDIENT_MEASURE);
                    String ingredient = ingredientAsJSONObject.getString(RECIPE_INGREDIENT_INGREDIENT);

                    // Create ingredient and add to list.
                    ingredientsAsList.add(new RecipeIngredient(quantity, measure, ingredient));
                }

                // Extract steps.
                ArrayList<RecipeStep> stepsAsList = new ArrayList<>();
                JSONArray stepsAsJSONArray = recipeAsJSONObject.getJSONArray(RECIPE_STEPS);
                for (int stepsIndex = 0; stepsIndex < stepsAsJSONArray.length(); stepsIndex++) {
                    // Get step within array.
                    JSONObject stepAsJSONObject = stepsAsJSONArray.getJSONObject(stepsIndex);

                    // Extract simple field information.
                    int stepID = stepAsJSONObject.getInt(RECIPE_STEP_ID);
                    String shortDescription = stepAsJSONObject.getString(RECIPE_STEP_SHORT_DESCRIPTION);
                    String description = stepAsJSONObject.getString(RECIPE_STEP_DESCRIPTION);
                    String videoURL = stepAsJSONObject.getString(RECIPE_STEP_VIDEO_URL);
                    String thumbnailURL = stepAsJSONObject.getString(RECIPE_STEP_THUMBNAIL_URL);

                    // Create step and add to list.
                    stepsAsList.add(new RecipeStep(stepID, shortDescription, description, videoURL, thumbnailURL));
                }

                // Create recipe and add to list.
                recipesAsList.add(new Recipe(recipeID, name, servings, image,  ingredientsAsList, stepsAsList));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return recipesAsList;
    }

    /**
     * Check if internet is available.
     * <p>TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.).</p>
     * <p>From answer to "How to check internet access on Android? InetAddress never times out" by Levite:
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out.</p>
     * @return True if available, otherwise false.
     */
    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }
}
