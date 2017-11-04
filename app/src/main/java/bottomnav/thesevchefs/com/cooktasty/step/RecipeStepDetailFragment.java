package bottomnav.thesevchefs.com.cooktasty.step;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import bottomnav.thesevchefs.com.cooktasty.MyApplication;
import bottomnav.thesevchefs.com.cooktasty.R;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {
    public static final String STEP_BUNDLE_KEY = "RECIPE_STEP_DATA";

    @BindView(R.id.videoContainer)
    SimpleExoPlayerView videoContainer;

    @BindView(R.id.defaultMediaImageView)
    ImageView defaultMediaImageView;

    @BindView(R.id.recipeStepDescriptionTextView)
    TextView recipeStepDescriptionTextView;

    RecipeInstruction step;
    SimpleExoPlayer player;
    Context context;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STEP_BUNDLE_KEY, Parcels.wrap(step));
    }

    @Nullable
    @Override
    public View onCreateView(
        LayoutInflater inflater,
        @Nullable ViewGroup container,
        Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.recipe_step_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            step = Parcels.unwrap(savedInstanceState.getParcelable(STEP_BUNDLE_KEY));
        }

        setup();

        return view;
    }

    public void setRecipeStep(RecipeInstruction step) {
        this.step = step;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setup() {
        // The thumbnail uri contains mp4 instead of image, so we'll load it into the video player
        if (step.image_url != null) {
            setupThumbnail(step);
        } else {
            videoContainer.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams layoutParams = videoContainer.getLayoutParams();
            layoutParams.height = 0;
            videoContainer.setLayoutParams(layoutParams);

            // Setup default view
            defaultMediaImageView.setVisibility(View.VISIBLE);
            Picasso
                    .with(getActivity())
                    .load(step.image_url)
                    .into(defaultMediaImageView);
        }

        recipeStepDescriptionTextView.setText(step.instruction);
    }

    private void setupThumbnail(RecipeInstruction step) {
        String url = step.image_url;
        Uri uri = Uri.parse(url);
        String extension = "";

//        int i = url.lastIndexOf('.');
//        if (i > 0) {
//            extension = url.substring(i + 1);
//        }
//
//        if (extension != "jpg" || extension != "png") {
//            return;
//        }

        Picasso
            .with(getActivity())
            .load(uri)
            .into(defaultMediaImageView);
    }


//    @TargetApi(Build.VERSION_CODES.M)
//    private void setupVideoPlayer(RecipeInstruction step) {
//        Context context = getContext();
//
//        videoContainer.setVisibility(View.VISIBLE);
//
//        if (player == null) {
//            // Create a default TrackSelector
//            Handler mainHandler = new Handler();
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
//            // Create the player
//            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//        }
//
//        videoContainer.setPlayer(player);
//
//        // Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
//            context,
//            Util.getUserAgent(context, "BakingApp")
//        );
//
//        // Produces Extractor instances for parsing the media data.
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//        // This is the MediaSource representing the media to be played.
//        MediaSource videoSource = new ExtractorMediaSource(
//            step.getVideoUri(),
//            dataSourceFactory,
//            extractorsFactory,
//            null,
//            null
//        );
//
//        player.prepare(videoSource);
//    }


    @Override
    public void onPause() {
        super.onPause();
        cleanupPlayer();
    }

    private void cleanupPlayer() {
        if (player == null) {
            return;
        }

        player.stop();
        player.release();
        player = null;
    }
}
