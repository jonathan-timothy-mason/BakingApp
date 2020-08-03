package jonathan.mason.baking_app.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Ingredient of recipe.
 */
public class RecipeIngredient implements Parcelable {
    /**
     * Constructor.
     * @param quantity Quantity of ingredient required for recipe.
     * @param measure Units of quantity.
     * @param ingredient The ingredient.
     */
    public RecipeIngredient(int quantity, String measure, String ingredient) {
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    private int mQuantity;
    /**
     * Get quantity.
     * @return Quantity.
     */
    public int getQuantity() {
        return mQuantity;
    }

    private String mMeasure;
    /**
     * Get units of quantity.
     * @return Units of quantity.
     */
    public String getMeasure() {
        return mMeasure;
    }

    private String mIngredient;
    /**
     * Get ingredient.
     * @return The ingredient.
     */
    public String getIngredient() {
        return mIngredient;
    }

    /********************************************
     * Implement Parcelable, including Creator. *
     ********************************************/

    /**
     * Interface needed to create ingredients from parcels.
     */
    public static final Parcelable.Creator<RecipeIngredient> CREATOR = new Parcelable.Creator<RecipeIngredient>() {
        /**
         * Create incredient from parcel.
         * @param in Ingredient as parcel.
         * @return Ingredient.
         */
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        /**
         * Create empty array of ingredients according to the specified size.
         * @param size Size of array to be created.
         * @return Array of ingredients.
         */
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
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
     * Create parcel from ingredient.
     * @param out Ingredient as parcel.
     * @param flags Flags to do with writing ingredient.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mQuantity);
        out.writeString(mMeasure);
        out.writeString(mIngredient);
    }

    /**
     * Constructor: create ingredient from parcel.
     * @param in Ingredient as parcel.
     */
    private RecipeIngredient(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }
}
