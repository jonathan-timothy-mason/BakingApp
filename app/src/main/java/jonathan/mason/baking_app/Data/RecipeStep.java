package jonathan.mason.baking_app.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Step of recipe.
 */
public class RecipeStep implements Parcelable {
    /**
     * Constructor.
     * @param id ID of recipe step.
     * @param shortDescription Short description of recipe step.
     * @param description Full description of recipe step.
     * @param videoURL URL of video of recipe step.
     * @param thumbnailURL URL of image of recipe step.
     */
    public RecipeStep(int id, String shortDescription, String description, String videoURL, String thumbnailURL)  {
        mID = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnailURL;
    }

    private int mID;
    /**
     * Get ID.
     * @return ID.
     */
    public int getID() {
        return mID;
    }

    private String mShortDescription;
    /**
     *  Get short description.
     * @return Short description.
     */
    public String getShortDescription() {
        return mShortDescription;
    }

    private String mDescription;
    /**
     * Get full description.
     * @return Full description.
     */
    public String getDescription() {
        return mDescription;
    }

    private String mVideoURL;
    /**
     * Get URL of video.
     * @return URL of video.
     */
    public String getVideoURL() {
        return mVideoURL;
    }

    private String mThumbnailURL;
    /**
     * Get URL of image.
     * @return URL of image.
     */
    public String getThumbNail() {
        return mThumbnailURL;
    }

    /********************************************
     * Implement Parcelable, including Creator. *
     ********************************************/

    /**
     * Interface needed to create steps from parcels.
     */
    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        /**
         * Create step from parcel.
         * @param in Step as parcel.
         * @return Step.
         */
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        /**
         * Create empty array of steps according to the specified size.
         * @param size Size of array to be created.
         * @return Array of Steps.
         */
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
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
     * Create parcel from step.
     * @param out Step as parcel.
     * @param flags Flags to do with writing step.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mID);
        out.writeString(mShortDescription);
        out.writeString(mDescription);
        out.writeString(mVideoURL);
        out.writeString(mThumbnailURL);
    }

    /**
     * Constructor: create step from parcel.
     * @param in Step as parcel.
     */
    private RecipeStep(Parcel in) {
        mID = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }
}
