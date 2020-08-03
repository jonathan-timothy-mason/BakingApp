package jonathan.mason.baking_app;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import jonathan.mason.baking_app.Data.Recipe;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static org.hamcrest.core.AllOf.allOf;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;

/**
 * Tests of MainActivity screen.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTests {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);

    /**
     * Test that clicking recipe generates expected intent.
     */
    @Test
    public void clickingRecyclerViewGeneratesExpectedIntent() {
        // Click first recipe.
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Ensure generated intent was for RecipeActivity and contains selected Recipe.
        intended(allOf(
                hasComponent(RecipeActivity.class.getName()),
                hasExtraWithKey(Recipe.SELECTED_RECIPE)
        ));
    }
}
