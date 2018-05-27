package com.letv.spo.mediaplayerex.PlayerFilter.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.letv.spo.mediaplayerex.PlayerFilter.SpoRenderer;
import com.letv.spo.mediaplayerex.PlayerFilter.chooser.ConfigChooser;
import com.letv.spo.mediaplayerex.PlayerFilter.contextfactory.ContextFactory;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private static final String PLAY_URL = "http://conan.los-cn-north-1.lecloudapis.com/conan_streaming/share/mediatest/normal/hls/1080p/1080.m3u8";
    private final List<FilterType> filterTypes = FilterType.createFilterList();
    private GLSurfaceView mPlayerView;
    private SpoRenderer spoRenderer;
    private MediaPlayer player;
    private Button button;
    private SeekBar seekBar;
    private SeekBar adjustBar;
    private TextView adjustTextView;
    private PlayerTimer playerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpGlPlayerView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
        if (playerTimer != null) {
            playerTimer.stop();
            playerTimer.removeMessages(0);
        }
    }

    private void setUpViews() {
        // play pause
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player == null) return;

                if (button.getText().toString().equals(MainActivity.this.getString(R.string.pause))) {
                    player.pause();
                    button.setText(R.string.play);
                } else {
                    player.start();
                    button.setText(R.string.pause);
                }
            }
        });

        //adjust
        adjustTextView = (TextView) findViewById(R.id.adjustValue);
        adjustBar = (SeekBar) findViewById(R.id.adjustBar);
        adjustBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int intensity, boolean fromUser) {
                if (player == null) {
                    return;
                }
                String adjustValue = "Intensity: " + intensity / 10;
                adjustTextView.setText(adjustValue);
                spoRenderer.setGlFilterIntensity(intensity / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // seek
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player == null) return;

                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                player.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        // list
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new FilterAdapter(this, R.layout.row_text, filterTypes));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spoRenderer.setGlFilter(FilterType.createGlFilter(filterTypes.get(position), getApplicationContext()));
                adjustBar.setProgress(0);
                adjustTextView.setText("Intensity: 0");
            }
        });
    }


    private void setUpGlPlayerView() {
        mPlayerView = new GLSurfaceView(this);
        mPlayerView.getHolder().addCallback(this);
        mPlayerView.setEGLContextFactory(new ContextFactory());
        mPlayerView.setEGLConfigChooser(new ConfigChooser());
        mPlayerView.setKeepScreenOn(true);
        ((FrameLayout) findViewById(R.id.video_loader)).addView(mPlayerView);

        spoRenderer = new SpoRenderer(mPlayerView);
        mPlayerView.setRenderer(spoRenderer);
    }

    private void setUpMediaPlayer() {
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        try {
            player.setDataSource(PLAY_URL);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setUpTimer() {
        playerTimer = new PlayerTimer();
        playerTimer.setCallback(new PlayerTimer.Callback() {
            @Override
            public void onTick(long timeMillis) {
                long position = player.getCurrentPosition();
                long duration = player.getDuration();

                if (duration <= 0) return;

                seekBar.setMax((int) duration / 1000);
                seekBar.setProgress((int) position / 1000);
            }
        });
        playerTimer.start();
    }


    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (mPlayerView != null) {
            spoRenderer.release();
            //the view can release opengl context
            mPlayerView.onPause();
            ((FrameLayout) findViewById(R.id.video_loader)).removeAllViews();
            mPlayerView = null;
        }
    }

    private int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setUpMediaPlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releasePlayer();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        ViewGroup.LayoutParams lp = ((FrameLayout) findViewById(R.id.video_loader)).getLayoutParams();
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = getScreenWidth(this);

        lp.width = screenWidth;
        lp.height = (int) ((float) screenWidth / videoProportion);
        ((FrameLayout) findViewById(R.id.video_loader)).setLayoutParams(lp);

        spoRenderer.setPlayer(player);
        spoRenderer.setGlFilter(FilterType.createGlFilter(FilterType.DEFAULT, this));
        mPlayerView.onResume();
        setUpTimer();
        player.start();
    }
}
