package jonathan.mason.baking_app.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ViewModel to load and persist recipes across configuration changes.
 * <p>Based on "Lifecycle Aware Data Loading with Architecture Components" by Ian Lake:
 * https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4.</p>
 */
public class RecipesViewModel extends AndroidViewModel {
    /**
     * Constructor.
     * @param application Baking app.
     */
    public RecipesViewModel(Application application){
        super(application);
        this.loadRecipes();
    }

    private final MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();
    /**
     * Get loaded recipes.
     * @return Loaded recipes.
     */
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    /**
     * Load recipes JSON string asynchronously from internet and convert to list of recipes
     * objects.
     * <p>Use of AsyncTask is safe as it has no reference to activities (see article mentioned
     * above).</p>
     */
    private void loadRecipes() {
        new AsyncTask<Void,Void,String>() {
            /**
             * Load recipes JSON string asynchronously from internet.
             * <p>Run on separate thread.</p>
             * @param voids Not used.
             * @return Recipes JSON string or null if there was a problem.
             */
            @Override
            protected String doInBackground(Void... voids) {
                if(DataUtils.isOnline()) {
                    try {
                        URL recipesURL = DataUtils.createRecipesURL();
                        HttpURLConnection connection = (HttpURLConnection)recipesURL.openConnection();
                        try {
                            InputStream stream = connection.getInputStream();
                            try {
                                // Read stream with Scanner using \A to read entire stream in one go,
                                // automatically handling buffering and UTF-8 to UTF-16 conversion.
                                Scanner scanner = new Scanner(stream);
                                scanner.useDelimiter("\\A");
                                if (scanner.hasNext()) {
                                    return scanner.next();
                                }
                            } finally {
                                stream.close();
                            }
                        } finally {
                            connection.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            /**
             * Convert loaded recipes JSON string to list of recipes objects.
             * <p>Run on main thread.</p>
             * @param recipesAsJSONString Recipes JSON string.
             */
            @Override
            protected void onPostExecute(String recipesAsJSONString) {
                if((recipesAsJSONString == null) || (recipesAsJSONString.isEmpty()))
                    mRecipes.setValue(new ArrayList<Recipe>()); // If there were no recipes, still allow empty collection to trigger update of live-data.
                else
                    mRecipes.setValue(DataUtils.parseRecipesJSON(recipesAsJSONString));
            }
        }.execute();
    }
}
