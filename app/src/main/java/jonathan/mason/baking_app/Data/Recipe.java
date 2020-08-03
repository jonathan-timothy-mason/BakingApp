package jonathan.mason.baking_app.Data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe, including ingredients and steps.
 */
public class Recipe implements Parcelable {
    /**
     * Keys for storing state in bundles.
     */
    public static final String SELECTED_RECIPE = "SELECTED_RECIPE";
    public static final String CURRENT_STEP_INDEX = "CURRENT_STEP_INDEX";

    /**
     * Constructor.
     * @param id ID of recipe.
     * @param name Name of recipe.
     * @param servings Number of servings made by recipe.
     * @param image URL of image of recipe.
     * @param ingredients Ingredients required for recipe.
     * @param steps Steps to make recipe.
     */
    public Recipe(int id, String name, int servings, String image, List<RecipeIngredient> ingredients, List<RecipeStep> steps) {
        mID = id;
        mName = name;
        mServings = servings;
        mImage = image;
        mIngredients = ingredients;
        mSteps = steps;
    }

    private int mID;
    /**
     * Get ID.
     * @return ID.
     */
    public int getID() {
        return mID;
    }

    private String mName;
    /**
     * Get name.
     * @return Name.
     */
    public String getName() {
        return mName;
    }

    private int mServings;
    /**
     * Get number of servings.
     * @return Number of servings.
     */
    public int getServings() {
        return mServings;
    }

    private String mImage;
    /**
     * Get URL of image.
     * @return URL of image.
     */
    public String getImage() {
        return mImage;
    }

    private List<RecipeIngredient> mIngredients;
    /**
     * Get ingredients.
     * @return Ingredients.
     */
    public List<RecipeIngredient> getIngredients() {
        return mIngredients;
    }

    /**
     * Get ingredients as a single string for display.
     * @param context Context.
     * @return Ingredients as string.
     */
    public String getIngredients(Context context) {
        String allIngredientsText = "";
        for(RecipeIngredient i: this.getIngredients()) {
            allIngredientsText += context.getString(jonathan.mason.baking_app.R.string.ingredient_bullet) + " " + i.getIngredient() + " " + i.getQuantity() + i.getMeasure() + "\n";
        }

        return allIngredientsText;
    }

    private List<RecipeStep> mSteps;
    /**
     * Get steps.
     * @return Steps.
     */
    public List<RecipeStep> getSteps() {
        return mSteps;
    }

    /********************************************
     * Implement Parcelable, including Creator. *
     ********************************************/

    /**
     * Interface needed to create recipes from parcels.
     */
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        /**
         * Create recipe from parcel.
         * @param in Recipe as parcel.
         * @return Recipe.
         */
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        /**
         * Create empty array of recipes according to the specified size.
         * @param size Size of array to be created.
         * @return Array of recipes.
         */
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Indicates any special objects in parcel.
     * @return Default 0, presumably to indicate no special objects.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Create parcel from recipe.
     * @param out Recipe as parcel.
     * @param flags Flags to do with writing recipe.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mID);
        out.writeString(mName);
        out.writeInt(mServings);
        out.writeString(mImage);
        out.writeTypedList(mIngredients);
        out.writeTypedList(mSteps);
    }

    /**
     * Constructor: create recipe from parcel.
     * @param in Recipe as parcel.
     */
    private Recipe(Parcel in) {
        mID = in.readInt();
        mName = in.readString();
        mServings = in.readInt();
        mImage = in.readString();
        mIngredients = new ArrayList<>();
        in.readTypedList(mIngredients, RecipeIngredient.CREATOR);
        mSteps = new  ArrayList<>();
        in.readTypedList(mSteps, RecipeStep.CREATOR);
    }
}
