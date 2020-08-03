package jonathan.mason.baking_app;

import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import jonathan.mason.baking_app.Data.Recipe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Tests of RecipeActivity screen.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityInstrumentedTests {
    @Rule
    public IntentsTestRule<RecipeActivity> mActivityRule = new IntentsTestRule<>(RecipeActivity.class, false, false);

    /**
     * Create intent with test recipe and launch RecipeActivity screen.
     */
    @Before
    public void launchActivity() {
        // Create intent with test recipe.
        Intent intent = new Intent(RecipeActivity.class.toString());
        intent.putExtra(Recipe.SELECTED_RECIPE, TestUtils.getTestRecipe());

        // Launch RecipeActivity screen.
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
     * Test that clicking ingredients generates expected intent.
     * <p>FOR PHONE LAYOUT ONLY.</p>
     */
    @Test
    public void clickingIngredientsInRecyclerViewGeneratesExpectedIntent() {
        if(!Utils.isTablet(mActivityRule.getActivity())) {
            // Click ingredients (at position 0 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            // Ensure generated intent was for IngredientsActivity and contains test Recipe.
            intended(allOf(
                    hasComponent(IngredientsActivity.class.getName()),
                    hasExtraWithKey(Recipe.SELECTED_RECIPE)
            ));
        }
    }

    /**
     * Test that clicking a recipe step generates expected intent.
     * <p>FOR PHONE LAYOUT ONLY.</p>
     */
    @Test
    public void clickingRecipeStepInRecyclerViewGeneratesExpectedIntent() {
        if(!Utils.isTablet(mActivityRule.getActivity())) {
            // Click last step (at position 3 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

            // Ensure generated intent was for RecipeStepDetailsActivity and contains test Recipe
            // and selected step index.
            intended(allOf(
                    hasComponent(RecipeStepDetailsActivity.class.getName()),
                    hasExtraWithKey(Recipe.SELECTED_RECIPE),
                    hasExtra(Recipe.CURRENT_STEP_INDEX, 2)
            ));
        }
    }

    /**
     * Test that ingredients are displayed by default.
     * <p>FOR TABLET LAYOUT ONLY.</p>
     */
    @Test
    public void ingredientsDisplayedByDefault() {
        if(Utils.isTablet(mActivityRule.getActivity())) {
            onView(withId(R.id.ingredients)).check(matches(withText("* Flour 100g\n* Cheese 200g\n* Tomatoes 300g\n")));
        }
    }

    /**
     * Test that clicking a recipe step displays expected recipe step.
     * <p>FOR TABLET LAYOUT ONLY.</p>
     */
    @Test
    public void clickingRecyclerViewDisplaysExpectedStep() {
        if(Utils.isTablet(mActivityRule.getActivity())) {
            // Click middle step (at position 2 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

            // Ensure recipe step details are as expected.
            onView(withId(R.id.description_caption)).check(matches(withText("Apply tomato")));
            onView(withId(R.id.description)).check(matches(withText("Spread tomatoes over bread once it has risen.")));
        }
    }

    /**
     * Test that clicking next button displays subsequent steps.
     * <p>FOR TABLET LAYOUT ONLY.</p>
     */
    @Test
    public void clickingNextButtonDisplaysExpectedStep() {
        if(Utils.isTablet(mActivityRule.getActivity())) {
            // Click first step (at position 1 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

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
    }

    /**
     * Test that clicking next button when already displaying last step has no effect.
     * <p>FOR TABLET LAYOUT ONLY.</p>
     */
    @Test
    public void clickingNextButtonWhenDisplayingLastStepHasNoEffect() {
        if(Utils.isTablet(mActivityRule.getActivity())) {
            // Start at last step (at position 3 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

            // Ensure recipe step details are as expected.
            onView(withId(R.id.description_caption)).check(matches(withText("Top with cheese")));
            onView(withId(R.id.description)).check(matches(withText("Liberally grate cheese over the top and bake for 15 minutes.")));

            // Click next button.
            onView(withId(R.id.next)).perform(click());

            // Ensure recipe step details are as expected, i.e. the same.
            onView(withId(R.id.description_caption)).check(matches(withText("Top with cheese")));
            onView(withId(R.id.description)).check(matches(withText("Liberally grate cheese over the top and bake for 15 minutes.")));
        }
    }

    /**
     * Test that clicking previous button displays subsequent steps.
     * <p>FOR TABLET LAYOUT ONLY.</p>
     */
    @Test
    public void clickingPreviousButtonDisplaysExpectedStep() {
        if(Utils.isTablet(mActivityRule.getActivity())) {
            // Start at last step (at position 3 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

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
    }

    /**
     * Test that clicking previous button when already displaying first step has no effect.
     * <p>FOR TABLET LAYOUT ONLY.</p>
     */
    @Test
    public void clickingPreviousButtonWhenDisplayingFirstStepHasNoEffect() {
        if(Utils.isTablet(mActivityRule.getActivity())) {
            // Click first step (at position 1 of RecyclerView).
            onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

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
}
