package jonathan.mason.baking_app;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

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
 * Tests of RecipeStepDetailsActivity screen.
 * <p>Although this activity should not ever be run on a tablet, it's fine to run
 * tests on phone or tablet.</p>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeStepDetailsActivityInstrumentedTests {
    @Rule
    public ActivityTestRule<RecipeStepDetailsActivity> mActivityRule = new ActivityTestRule<>(RecipeStepDetailsActivity.class, false, false);

    /**
     * Create intent with test recipe and selected recipe step index and launch
     * RecipeStepDetailsActivity screen.
     * <p>To be called by each test with required selected step index.</p>
     * @param selectedStepIndex Index of recipe step to display.
     */
    public void launchActivity(int selectedStepIndex) {
        // Create intent with test recipe.
        Intent intent = new Intent(RecipeStepDetailsActivity.class.toString());
        intent.putExtra(Recipe.SELECTED_RECIPE, TestUtils.getTestRecipe());
        intent.putExtra(Recipe.CURRENT_STEP_INDEX, selectedStepIndex);

        // Launch RecipeStepDetailsActivity screen.
        mActivityRule.launchActivity(intent);
    }

    /**
     * Test activity screen has recipe and servings in title.
     * <p>"hasDescendant" from answer to "How to check Toolbar title in android instrumental test?" by Ugo:
     * https://stackoverflow.com/questions/36329978/how-to-check-toolbar-title-in-android-instrumental-test.</p>
     */
    @Test
    public void recipeDisplayedAsTitle() {
        this.launchActivity(0); // Select any step.
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Pizza (Serves 4)"))));
    }

    /**
     * Test that details of selected step are displayed.
     */
    @Test
    public void detailsOfSelectedStepDisplayed() {
        this.launchActivity(1); // Select middle step.

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Apply tomato")));
        onView(withId(R.id.description)).check(matches(withText("Spread tomatoes over bread once it has risen.")));
    }

    /**
     * Test that clicking next button displays subsequent steps.
     */
    @Test
    public void clickingNextButtonDisplaysExpectedStep() {
        this.launchActivity(0); // Select first step.

        // Ensure initial recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Make bread")));
        onView(withId(R.id.description)).check(matches(withText("Mix the flour with a tablespoon of water, a pinch of salt and yeast.")));

        // Click next button.
        onView(withId(R.id.next)).perform(click());

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Apply tomato")));
        onView(withId(R.id.description)).check(matches(withText("Spread tomatoes over bread once it has risen.")));

        // Click next button.
        onView(withId(R.id.next)).perform(click());

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Top with cheese")));
        onView(withId(R.id.description)).check(matches(withText("Liberally grate cheese over the top and bake for 15 minutes.")));
    }

    /**
     * Test that clicking next button when already displaying last step has no effect.
     */
    @Test
    public void clickingNextButtonWhenDisplayingLastStepHasNoEffect() {
        this.launchActivity(2); // Select last step.

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Top with cheese")));
        onView(withId(R.id.description)).check(matches(withText("Liberally grate cheese over the top and bake for 15 minutes.")));

        // Click next button.
        onView(withId(R.id.next)).perform(click());

        // Ensure recipe step details are as expected, i.e. the same.
        onView(withId(R.id.description_caption)).check(matches(withText("Top with cheese")));
        onView(withId(R.id.description)).check(matches(withText("Liberally grate cheese over the top and bake for 15 minutes.")));
    }

    /**
     * Test that clicking previous button displays subsequent steps.
     */
    @Test
    public void clickingPreviousButtonDisplaysExpectedStep() {
        this.launchActivity(2); // Select last step.

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Top with cheese")));
        onView(withId(R.id.description)).check(matches(withText("Liberally grate cheese over the top and bake for 15 minutes.")));

        // Click previous button.
        onView(withId(R.id.previous)).perform(click());

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Apply tomato")));
        onView(withId(R.id.description)).check(matches(withText("Spread tomatoes over bread once it has risen.")));

        // Click previous button.
        onView(withId(R.id.previous)).perform(click());

        // Ensure recipe step details are as expected.
        onView(withId(R.id.description_caption)).check(matches(withText("Make bread")));
        onView(withId(R.id.description)).check(matches(withText("Mix the flour with a tablespoon of water, a pinch of salt and yeast.")));
    }

    /**
     * Test that clicking previous button when already displaying first step has no effect.
     */
    @Test
    public void clickingPreviousButtonWhenDisplayingFirstStepHasNoEffect() {
        this.launchActivity(0); // Select first step.

        // Ensure recipe step details are as expected, i.e. the first step.
        onView(withId(R.id.description_caption)).check(matches(withText("Make bread")));
        onView(withId(R.id.description)).check(matches(withText("Mix the flour with a tablespoon of water, a pinch of salt and yeast.")));

        // Click previous button.
        onView(withId(R.id.previous)).perform(click());

        // Ensure recipe step details are as expected, i.e. the same.
        onView(withId(R.id.description_caption)).check(matches(withText("Make bread")));
        onView(withId(R.id.description)).check(matches(withText("Mix the flour with a tablespoon of water, a pinch of salt and yeast.")));
    }
}
