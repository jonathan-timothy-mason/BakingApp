package jonathan.mason.baking_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jonathan.mason.baking_app.Data.Recipe;

public class IngredientsFragment extends Fragment {
    @BindView(R.id.ingredients) TextView mIngredientsTextView;
    private Unbinder mUnbinder;
    private Recipe mRecipe;

    /**
     * Constructor.
     * <P>Default constructor required for instantiation by Android framework.</P>
     */
    public IngredientsFragment() { }

    /**
     * Override to ensure activity is suitable for use with fragment.
     *
     * @param context Not used.
     * @throws RuntimeException Thrown if intent does not contain "Recipe.SELECTED_RECIPE"
     *                          key within extra data.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Make sure intent of activity contains "Recipe.SELECTED_RECIPE" key
        // within extra data.
        if (!this.getActivity().getIntent().hasExtra(Recipe.SELECTED_RECIPE))
            throw new RuntimeException("No recipe passed to " + this.toString());
    }

    /**
     * Create root view of fragment, inflating its layout.
     *
     * @param inflater           Inflater to inflate fragment.
     * @param container          Parent view for fragment.
     * @param savedInstanceState Saved state of fragment; not used.
     * @return Root view of fragment.
     * @throws RuntimeException Thrown if index of current recipe step is invalid.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Retrieve selected recipe passed to host activity screen.
        mRecipe = this.getActivity().getIntent().getParcelableExtra(Recipe.SELECTED_RECIPE);

        // Inflate fragment.
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        // Retrieve view(s).
        mUnbinder = ButterKnife.bind(this, rootView);

        // Set ingredients.
        mIngredientsTextView.setText(mRecipe.getIngredients(this.getActivity()));

        return rootView;
    }

    /**
     * Override to allow ButterKnife to "unbind" views, which is only necessary for fragments.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}