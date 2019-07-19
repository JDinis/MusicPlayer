package pt.jdinis.musicplayer;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static pt.jdinis.musicplayer.PlayerService.ACTION_START_PLAYER_SERVICE;
import static pt.jdinis.musicplayer.PlayerService.ACTION_STOP;

public class MusicPlayerActivity extends AppCompatActivity {
    ImageButton playBtn, prevBtn, nextBtn;
    ImageView albumImageView;
    TextView currentTimeTextView, durationTextView;
    SeekBar seekBar;
    Intent intent;

    private View.OnClickListener playListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            if (PlayerService.getMediaPlayer() != null && PlayerService.getMediaPlayer().isPlaying()) {
                playBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                Intent pauseIntent = new Intent(view.getContext(), PlayerService.class);
                pauseIntent.setAction(PlayerService.ACTION_PAUSE);

                try {
                    PendingIntent.getForegroundService(view.getContext(), 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            } else {
                if (PlayerService.getMediaPlayer() != null && PlayerService.getMediaPlayer().getCurrentPosition() > 0) {

                    Intent startIntent = new Intent(view.getContext(), PlayerService.class);
                    startIntent.setAction(PlayerService.ACTION_RESUME);

                    try {
                        PendingIntent.getForegroundService(view.getContext(), 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                } else {
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    SongItem songItem = (SongItem) intent.getSerializableExtra(PlayerService.SONG_ITEM);
                    albumImageView.setImageBitmap(songItem.getAlbumImage(getResources()));

                    int duration = Integer.parseInt(songItem.getDuration());

                    durationTextView.setText(String.format("%02d:%02d", duration / 60000, ((duration / 1000) - (60 * (duration / 60000)))));
                    seekBar.setMax(duration);

                    Intent startIntent = new Intent(view.getContext(), PlayerService.class);
                    startIntent.setAction(PlayerService.ACTION_PLAY);
                    startIntent.putExtra(PlayerService.SONG_ITEM, songItem);

                    try {
                        PendingIntent.getForegroundService(view.getContext(), 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private View.OnClickListener prevListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            SongItem songItem = (SongItem) getIntent().getSerializableExtra(PlayerService.SONG_ITEM + 1);

            if (songItem != null) {
                MediaDescriptionCompat.Builder builder = new MediaDescriptionCompat.Builder();
                builder.setMediaUri(songItem.getUri());
                builder.setTitle(songItem.getTitle());
                builder.setSubtitle(songItem.getArtist());
                PlayerService.getMediaSession().getController().addQueueItem(builder.build());
            }
            Intent pauseIntent = new Intent(view.getContext(), PlayerService.class);
            pauseIntent.setAction(PlayerService.ACTION_PREVIOUS);
            PendingIntent.getForegroundService(view.getContext(), 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    };

    private View.OnClickListener nextListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            SongItem songItem = (SongItem) getIntent().getSerializableExtra(PlayerService.SONG_ITEM + 2);

            if (songItem != null) {
                MediaDescriptionCompat.Builder builder = new MediaDescriptionCompat.Builder();
                builder.setMediaUri(songItem.getUri());
                builder.setTitle(songItem.getTitle());
                builder.setSubtitle(songItem.getArtist());
                PlayerService.getMediaSession().getController().addQueueItem(builder.build());
            }
            Intent pauseIntent = new Intent(view.getContext(), PlayerService.class);
            pauseIntent.setAction(PlayerService.ACTION_NEXT);
            try {
                PendingIntent.getForegroundService(view.getContext(), 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    };
    private OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (PlayerService.getMediaPlayer() != null) {
                PlayerService.getMediaPlayer().seekTo(i);
                PlayerService.setCurrentPosition(i);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            int duration = seekBar.getProgress();
            currentTimeTextView.setText(String.format("%02d:%02d", duration / 60000, ((duration / 1000) - (60 * (duration / 60000)))));
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int duration = seekBar.getProgress();

            if (PlayerService.getMediaPlayer() != null)
                PlayerService.getMediaPlayer().seekTo(duration);

            currentTimeTextView.setText(String.format("%02d:%02d", duration / 60000, ((duration / 1000) - (60 * (duration / 60000)))));
        }
    };

    public static MusicPlayerActivity newInstance() {
        return new MusicPlayerActivity();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        Intent notificationIntent = new Intent(this, PlayerService.class);
        notificationIntent.setAction(ACTION_START_PLAYER_SERVICE);

        SongItem songItem = ((SongAdapter) Home.getRecyclerView().getAdapter()).getSongItem();
        notificationIntent.putExtra(PlayerService.SONG_ITEM, songItem);

        try {
            PendingIntent.getForegroundService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        if (PlayerService.getMediaPlayer() != null && PlayerService.getMediaPlayer().isPlaying()) {
            Intent notificationIntent = new Intent(this, PlayerService.class);
            notificationIntent.setAction(ACTION_STOP);

            SongItem songItem = ((SongAdapter) Home.getRecyclerView().getAdapter()).getSongItem();
            notificationIntent.putExtra(PlayerService.SONG_ITEM, songItem);

            try {
                PendingIntent.getForegroundService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        Intent notificationIntent = new Intent(this, PlayerService.class);
        notificationIntent.setAction(ACTION_START_PLAYER_SERVICE);

        SongItem songItem = ((SongAdapter) Home.getRecyclerView().getAdapter()).getSongItem();
        notificationIntent.putExtra(PlayerService.SONG_ITEM, songItem);

        try {
            PendingIntent.getForegroundService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_music_player);

        albumImageView = findViewById(R.id.albumImageView);

        playBtn = findViewById(R.id.playButton);
        prevBtn = findViewById(R.id.prevButton);
        nextBtn = findViewById(R.id.nextButton);

        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        durationTextView = findViewById(R.id.durationTextView);

        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        playBtn.setOnClickListener(playListener);
        prevBtn.setOnClickListener(prevListener);
        nextBtn.setOnClickListener(nextListener);

        intent = getIntent();
    }
}
