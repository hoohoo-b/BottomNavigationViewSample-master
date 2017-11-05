package bottomnav.thesevchefs.com.cooktasty.step;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bottomnav.thesevchefs.com.cooktasty.MyApplication;
import bottomnav.thesevchefs.com.cooktasty.R;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;
import bottomnav.thesevchefs.com.cooktasty.utilities.ImageToVideoConverter;
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

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.time_remaining)
    TextView mTimeRemaining;

    CountDownTimer mCountDownTimer;
    int i = 0;

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

        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setRecipeStep(RecipeInstruction step) {
        this.step = step;
    }

    private void setup() throws IOException {
        // The thumbnail uri contains mp4 instead of image, so we'll load it into the video player
//        if (step.image_url != null) {
            setupThumbnail(step);
//            File file = ImageToVideoConverter.setvideo(getActivity(), step.image_url);
//            setupVideoPlayer(file, step);
//        } else {
//            videoContainer.setVisibility(View.INVISIBLE);
//            ViewGroup.LayoutParams layoutParams = videoContainer.getLayoutParams();
//            layoutParams.height = 0;
//            videoContainer.setLayoutParams(layoutParams);
//
//            // Setup default view
//            defaultMediaImageView.setVisibility(View.VISIBLE);
//            Picasso
//                    .with(getActivity())
//                    .load(R.drawable.cooktasty_logo)
//                    .into(defaultMediaImageView);
//        }
//
        recipeStepDescriptionTextView.setText(step.instruction);
    }

    private void setupThumbnail(RecipeInstruction step) {
        if (step.image_url != null) {
            String url = step.image_url;
            Uri uri = Uri.parse(url);
//            String extension = "";

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

        } else {
            defaultMediaImageView.setImageDrawable(getResources().getDrawable(R.drawable.cooktasty_logo));
        }

        mProgressBar.setProgress(i);
        final int time = (int) step.time_required.getTime();
        mCountDownTimer = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressBar.setProgress((int) i * 100 / (time / 1000));
                mTimeRemaining.setText(String.format("%d h %d min %d s",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(100);
                mTimeRemaining.setText("0 h 0 min 0 s");
            }
        };
        mCountDownTimer.start();
    }

    private void setupVideoPlayer(File file, RecipeInstruction step) throws IOException {
        Context context = getActivity();

        Uri step_video = Uri.fromFile(file);

        videoContainer.setVisibility(View.VISIBLE);

        if (player == null) {
            // Create a default TrackSelector
            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        }


        Bitmap bitmap = ImageToVideoConverter.getBitmapFromURL(step.image_url);

        videoContainer.setPlayer(player);
        videoContainer.setDefaultArtwork(bitmap);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, "CookTasty")
        );

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(
                step_video,
                dataSourceFactory,
                extractorsFactory,
                null,
                null
        );

        player.prepare(videoSource);
    }


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
