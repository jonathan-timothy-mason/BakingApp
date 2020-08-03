package jonathan.mason.baking_app;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import jonathan.mason.baking_app.Data.Recipe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests of IngredientsActivity screen.
 * <p>Although this activity should not ever be run on a tablet, it's fine to run
 * tests on phone or tablet.</p>
 */
@RunWith(AndroidJUnit4.class)
public class IngredientsActivityInstrumentedTests {
    @Rule
    public ActivityTestRule<IngredientsActivity> mActivityRule = new ActivityTestRule<>(IngredientsActivity.class, false, false);

    /**
     * Create intent with test recipe and launch IngredientsActivity screen.
     */
    @Before
    public void launchActivity() {
        // Create intent with test recipe.
        Intent intent = new Intent(RecipeStepDetailsActivity.class.toString());
        intent.putExtra(Recipe.SELECTED_RECIPE, TestUtils.getTestRecipe());

        // Launch IngredientsActivity screen.
        mActivityRule.launchActivity(intent);
    }

    /**
     * Test activity screen has recipe and servings in title.
     * <p>"hasDescendant" from answer to "How to check Toolbar title in android instrumental test?" by Ugo:
     * https://stackoverflow.com/questions/36329978/how-to-check-toolbar-title-in-android-instrumental-test.</p>
     */
    @Test
    public void recipeDisplayedAsTitle() {
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Pizza (Serves 4)"))));
    }

    /**
     * Test that ingredients of test recipe are displayed as expected.
     */
    @Test
    public void displayedIngredientsCorrect() {

        onView(withId(R.id.ingredients)).check(matches(withText("* Flour 100g\n* Cheese 200g\n* Tomatoes 300g\n")));
    }
}
