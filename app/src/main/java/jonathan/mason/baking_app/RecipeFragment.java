package jonathan.mason.baking_app;

import android.content.Context;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jonathan.mason.baking_app.Data.Recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment to display ingredients and steps of recipe.
 */
public class RecipeFragment extends Fragment {

    @BindView(R.id.recipe_steps_recycler_view) RecyclerView mRecipeStepsRecyclerView;
    private Unbinder mUnbinder;

    /**
     * Constructor.
     * <P>Default constructor required for instantiation by Android framework.</P>
     */
    public RecipeFragment() { }

    /**
     * Override to ensure activity is suitable for use with fragment.
     * @param context Not used.
     * @exception ClassCastException Thrown if activity does not implement "RecipeStepsAdapter.RecipeStepSelectionListener"
     * @exception RuntimeException Thrown if intent does not contain "Recipe.SELECTED_RECIPE"
     * key within extra data.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Make sure activity implements RecipeStepsAdapter.RecipeStepSelectionListener.
        try {
            RecipeStepsAdapter.RecipeStepSelectionListener listener = (RecipeStepsAdapter.RecipeStepSelectionListener)this.getActivity();
        }
        catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " does not implement RecipeStepsAdapter.RecipeStepSelectionListener.");
        }

        // Make sure intent of activity contains "Recipe.SELECTED_RECIPE" key
        // within extra data.
        if(!this.getActivity().getIntent().hasExtra(Recipe.SELECTED_RECIPE))
            throw new RuntimeException("No recipe passed to " + this.toString());
    }

    /**
     * Create root view of fragment, inflating its layout.
     * @param inflater Inflater to inflate fragment.
     * @param container Parent view for fragment.
     * @param savedInstanceState Saved state of fragment; not used.
     * @return Root view of fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment.
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Retrieve view(s).
        mUnbinder = ButterKnife.bind(this, rootView);

        // Retrieve selected recipe passed to host activity screen.
        Recipe recipe = this.getActivity().getIntent().getParcelableExtra(Recipe.SELECTED_RECIPE);

        // Set up to use GridLayoutManager (recipes adapter created after recipes
        // have loaded).
        mRecipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        // Create recipe steps adapter.
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(recipe.getSteps(),(RecipeStepsAdapter.RecipeStepSelectionListener)this.getActivity());

        // Set adapter of RecycleView.
        mRecipeStepsRecyclerView.setAdapter(recipeStepsAdapter);

        return rootView;
    }

    /**
     * Override to allow ButterKnife to "unbind" views, which is only necessary
     * for fragments.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
