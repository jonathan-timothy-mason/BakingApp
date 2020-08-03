package jonathan.mason.baking_app;

import android.content.Context;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jonathan.mason.baking_app.Data.Recipe;
import jonathan.mason.baking_app.Data.RecipeStep;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailsFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {
    /**
     * Keys for storing state in bundles.
     */
    public static final String VIDEO_PLAYER_POSITION = "VIDEO_PLAYER_POSITION";
    public static final String VIDEO_PLAYER_STATE = "VIDEO_PLAYER_STATE";

    @BindView(R.id.description_caption) TextView mDescriptionCaptionTextView;
    @BindView(R.id.description) TextView mDescriptionTextView;
    @BindView(R.id.previous) ImageButton mPreviousButton;
    @BindView(R.id.next) ImageButton mNextButton;
    @BindView(R.id.video_player_container) FrameLayout mVideoPlayerViewContainer;
    @BindView(R.id.video_player_view) SimpleExoPlayerView mVideoPlayerView;
    private Unbinder mUnbinder;
    private SimpleExoPlayer mVideoPlayer;
    private Recipe mRecipe;
    private int mCurrentStepIndex;
    private long mSavedVideoPlayerPosition;
    private Boolean mSavedVideoPlayerState;

    /**
     * Constructor.
     * <P>Default constructor required for instantiation by Android framework.</P>
     */
    public RecipeStepDetailsFragment() { }

    /**
     * Override to ensure activity is suitable for use with fragment.
     * @param context Not used.
     * @exception RuntimeException Thrown if intent does not contain "Recipe.SELECTED_RECIPE"
     * key within extra data.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Make sure intent of activity contains "Recipe.SELECTED_RECIPE" key
        // within extra data.
        if(!this.getActivity().getIntent().hasExtra(Recipe.SELECTED_RECIPE))
            throw new RuntimeException("No recipe passed to " + this.toString());
    }

    /**
     * Create root view of fragment, inflating its layout.
     * @param inflater Inflater to inflate fragment.
     * @param container Parent view for fragment.
     * @param savedInstanceState Saved state of fragment.
     * @return Root view of fragment.
     * @exception RuntimeException Thrown if index of current recipe step is invalid.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCurrentStepIndex = 0;
        mSavedVideoPlayerPosition = 0;
        mSavedVideoPlayerState = false; // Default to not playing.
        if(savedInstanceState == null) {
            // Get index of selected step from activity, first time fragment is created,
            // if present.
            if (this.getActivity().getIntent().hasExtra(Recipe.CURRENT_STEP_INDEX))
                mCurrentStepIndex = this.getActivity().getIntent().getIntExtra(Recipe.CURRENT_STEP_INDEX, 0);
        }
        else {
            // Restore index of current step, if applicable.
            if (savedInstanceState.containsKey(Recipe.CURRENT_STEP_INDEX))
                mCurrentStepIndex = savedInstanceState.getInt(Recipe.CURRENT_STEP_INDEX, 0);

            // Restore position and state of video player.
            if(savedInstanceState.containsKey(VIDEO_PLAYER_POSITION))
                mSavedVideoPlayerPosition = savedInstanceState.getLong(VIDEO_PLAYER_POSITION, 0);
            if(savedInstanceState.containsKey(VIDEO_PLAYER_STATE))
                mSavedVideoPlayerState = savedInstanceState.getBoolean(VIDEO_PLAYER_STATE, false);
        }

        // Retrieve selected recipe passed to host activity screen.
        mRecipe = this.getActivity().getIntent().getParcelableExtra(Recipe.SELECTED_RECIPE);

        // Make sure index of current step is valid.
        if((mCurrentStepIndex < 0) || (mCurrentStepIndex >= mRecipe.getSteps().size()))
            throw new RuntimeException("Current recipe step index is invalid: " + mCurrentStepIndex);

        // Inflate fragment.
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        // Retrieve view(s).
        mUnbinder = ButterKnife.bind(this, rootView);

        // Set fragment to be button click listener.
        mPreviousButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);

        // Display details of selected recipe step.
        this.initialiseRecipeStep();

        return rootView;
    }

    /**
     * Initialise currently displayed recipe step to that of current index.
     */
    private void initialiseRecipeStep() {
        // Set caption and text to current text.
        RecipeStep currentStep = mRecipe.getSteps().get(mCurrentStepIndex);
        mDescriptionCaptionTextView.setText(currentStep.getShortDescription());
        mDescriptionTextView.setText(currentStep.getDescription());

        if((currentStep.getVideoURL() != null) && !currentStep.getVideoURL().isEmpty())
            mVideoPlayerViewContainer.setVisibility(View.VISIBLE); // Ensure video player is showing, in case it was hidden.
        else
            mVideoPlayerViewContainer.setVisibility(View.GONE); // Hide video player if there is no video.
    }

    /**
     * Update currently displayed recipe step to that of specified index.
     * @param newCurrentStepIndex Index of new recipe step.
     */
    public void updateRecipeStep(int newCurrentStepIndex) {
        if((mCurrentStepIndex != newCurrentStepIndex) && (newCurrentStepIndex >= 0) && (newCurrentStepIndex < mRecipe.getSteps().size())) {
            mCurrentStepIndex = newCurrentStepIndex;

            this.initialiseRecipeStep();

            // Stop last video playing and clear saved position and state of last step.
            mSavedVideoPlayerPosition = 0;
            mSavedVideoPlayerState = false;
            mVideoPlayer.stop();

            // Play video of new recipe step, if there is a video.
            this.loadRecipeStepVideo();
        }
    }

    /**
     * Setup video player.
     */
    private void initialisePlayer() {
        mVideoPlayer = ExoPlayerFactory.newSimpleInstance(this.getActivity(), new DefaultTrackSelector(), new DefaultLoadControl());
        mVideoPlayerView.setPlayer(mVideoPlayer);
        mVideoPlayer.addListener(this);

        this.loadRecipeStepVideo();
    }

    /**
     * Release video player.
     */
    private void releasePlayer() {
        mVideoPlayer.stop();
        mVideoPlayer.release();
        mVideoPlayer = null;
    }

    /**
     * Load video of current recipe step, if there is a video.
     */
    private void loadRecipeStepVideo() {
        if((mCurrentStepIndex >= 0) && (mCurrentStepIndex < mRecipe.getSteps().size())) {
            RecipeStep currentStep = mRecipe.getSteps().get(mCurrentStepIndex);
            if((currentStep.getVideoURL() != null) && !currentStep.getVideoURL().isEmpty()) {
                Uri uri = Uri.parse(currentStep.getVideoURL());
                String userAgent = Util.getUserAgent(this.getActivity(), this.getString(R.string.app_name));
                MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(this.getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
                mVideoPlayer.prepare(mediaSource);
                mVideoPlayer.seekTo(mSavedVideoPlayerPosition);
                mVideoPlayer.setPlayWhenReady(mSavedVideoPlayerState);
            }
        }
    }

    /**
     * Override to setup video player.
     * <p>From Android API version 24, video player should only be released when app stopped,
     * not paused, as this version of API allows a paused app to be visible when displayed
     * side by side with another app. Therefore, if video player released when stopped, it
     * must be initialised when started.</p>
     */
    @Override
    public void onStart() {
        super.onStart();

        if(Util.SDK_INT >= 24)
            this.initialisePlayer();
    }

    /**
     * Override to setup video player.
     * <p>Before Android API version 24, video player can be released when app paused, as
     * app cannot be visible. Therefore, if video player released when paused, it must be
     * initialised when resumed.</p>
     */
    @Override
    public void onResume() {
        super.onResume();

        if(Util.SDK_INT < 24)
            this.initialisePlayer();
    }

    /**
     * Override to release video player as early as possible.
     * <p>Before Android API version 24, video player can be released when app paused, as
     * app cannot be visible.</p>
     */
    @Override
    public void onPause() {
        super.onPause();

        if(Util.SDK_INT < 24)
            this.releasePlayer();
    }

    /**
     * Override to save index of current recipe step.
     * @param outState Saved state of fragment.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mSavedVideoPlayerPosition = mVideoPlayer.getCurrentPosition();
        mSavedVideoPlayerState = mVideoPlayer.getPlayWhenReady();
        outState.putInt(Recipe.CURRENT_STEP_INDEX, mCurrentStepIndex);
        outState.putLong(VIDEO_PLAYER_POSITION, mSavedVideoPlayerPosition);
        outState.putBoolean(VIDEO_PLAYER_STATE, mSavedVideoPlayerState);
    }

    /**
     * Override to release video palyer as early as possible.
     * <p>From Android API version 24, video player should only be released when app stopped,
     * not paused, as this version of API allows a paused app to be visible when displayed
     * side by side with another app.</p>
     */
    @Override
    public void onStop() {
        super.onStop();

        if(Util.SDK_INT >= 24)
            this.releasePlayer();
    }

    /**
     * Override to allow ButterKnife to "unbind" views, which is only necessary for fragments.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    /***********************************
     * Implement View.OnClickListener. *
     ***********************************/

    /**
     * Handle click of previous and next buttons to update current recipe step.
     * @param view Clicked previous or next button.
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.previous)
            this.updateRecipeStep(mCurrentStepIndex - 1);
        else
            this.updateRecipeStep(mCurrentStepIndex + 1);
    }

    /************************************************
     * Implement ExoPlayer.EventListener interface. *
     ************************************************/

    /**
     * Implemented, but not used.
     */
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) { /* Do nothing. */ }

    /**
     * Implemented, but not used.
     */
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { /* Do nothing. */ }

    /**
     * Implemented, but not used.
     */
    @Override
    public void onLoadingChanged(boolean isLoading) { /* Do nothing. */ }

    /**
     * Overridden to hide video player after it has finished playing, but only for phone
     * orientated to landscape.
     * @param playWhenReady True if playing, false if paused.
     * @param playbackState State of video player: STATE_READY, STATE_IDLE, STATE_BUFFERING or
     * STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playWhenReady && (playbackState == ExoPlayer.STATE_ENDED)) {
            // Reset video.
            mVideoPlayer.seekTo(0);
            mVideoPlayer.setPlayWhenReady(false);

            // For phone in landscape, hide video to reveal recipe steps when video has finished.
            if((Utils.isTablet(this.getActivity()) == false) && Utils.isLandscape(this.getActivity()))
                mVideoPlayerViewContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Implemented, but not used.
     */
    @Override
    public void onPlayerError(ExoPlaybackException error) { /* Do nothing. */ }

    /**
     * Implemented, but not used.
     */
    @Override
    public void onPositionDiscontinuity() { /* Do nothing. */ }
}
