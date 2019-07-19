package pt.jdinis.musicplayer;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import static android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY;
import static android.media.AudioManager.ACTION_HEADSET_PLUG;

public class PlayerService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        AudioManager.OnAudioFocusChangeListener {
    protected static final String ACTION_START_PLAYER_SERVICE = "ACTION_START_PLAYER_SERVICE";
    protected static final String ACTION_STOP_PLAYER_SERVICE = "ACTION_STOP_PLAYER_SERVICE";
    protected static final String ACTION_PLAY = "ACTION_PLAY";
    protected static final String ACTION_PAUSE = "ACTION_PAUSE";
    protected static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    protected static final String ACTION_NEXT = "ACTION_NEXT";
    protected static final String ACTION_SEEKTO = "ACTION_SEEKTO";
    protected static final String ACTION_RESUME = "ACTION_RESUME";
    protected static final String ACTION_STOP = "ACTION_STOP";
    protected static final String ACTION_MEDIA = "android.media.browse.MediaBrowserService";
    protected static final String SONG_ITEM = "SONG_ITEM";
    private static final String TAG_PLAYER_SERVICE = "PLAYER_SERVICE";
    private static final String MEDIASESSION_TAG = "MusicPlayerMediaSession";
    private static int NOTIFICATION_ID = 1;
    private static MediaPlayer mediaPlayer;
    private static MediaSessionCompat mediaSession;
    private static PlaybackStateCompat.Builder stateBuilder;
    private static AudioFocusRequest audioFocusRequest;
    private static NoiseAudioReciever noiseAudioReciever = new NoiseAudioReciever();
    private static int currentPosition;
    private static Context context;
    private static Intent intent;
    private static MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @TargetApi(Build.VERSION_CODES.O)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPlay() {
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

            // Request audio focus for playback, this registers the afChangeListener
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener((PlayerService) getContext())
                    .setAudioAttributes(attrs)
                    .build();

            int result = am.requestAudioFocus(audioFocusRequest);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                // Set the session active  (and update metadata and state)
                mediaSession.setActive(true);

                // start the player (custom call)
                mediaPlayer.start();

                // Register BECOME_NOISY BroadcastReceiver
                getContext().registerReceiver(noiseAudioReciever, new IntentFilter(ACTION_AUDIO_BECOMING_NOISY));
                getContext().registerReceiver(noiseAudioReciever, new IntentFilter(ACTION_HEADSET_PLUG));

                // Start the service
                startForeground();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onStop() {
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

            // Abandon audio focus
            am.abandonAudioFocusRequest(audioFocusRequest);

            try {
                getContext().unregisterReceiver(noiseAudioReciever);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Stop the service
            ((PlayerService) getContext()).stopSelf();

            // Set the session inactive  (and update metadata and state)
            mediaSession.setActive(false);

            // stop the player (custom call)
            mediaPlayer.stop();

            mediaPlayer.reset();

            // Take the service out of the foreground
            try {
                ((PlayerService) getContext()).stopForeground(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPause() {
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

            // Update metadata and state
            // pause the player (custom call)
            mediaPlayer.pause();

            currentPosition = mediaPlayer.getCurrentPosition();

            // unregister BECOME_NOISY BroadcastReceiver
            try {
                getContext().unregisterReceiver(noiseAudioReciever);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Take the service out of the foreground, retain the notification
            try {
                ((PlayerService) getContext()).stopForeground(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public static Context getContext() {
        return context;
    }

    public static MediaSessionCompat getMediaSession() throws NullPointerException {
        return mediaSession;
    }

    public static MediaPlayer getMediaPlayer() throws NullPointerException {
        return mediaPlayer;
    }

    private static void createMediaSession(Context context) {
        if (mediaSession == null) {
            mediaSession = new MediaSessionCompat(context, MEDIASESSION_TAG);
            mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);
            stateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE |
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                            PlaybackStateCompat.ACTION_STOP
                    );
            mediaSession.setPlaybackState(stateBuilder.build());
            mediaSession.setCallback(callback);
            //mediaSession.setMediaButtonReceiver(null);
        }
    }

    private static void createPlayer(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnCompletionListener((PlayerService) context);
            mediaPlayer.setOnPreparedListener((PlayerService) context);
            mediaPlayer.setOnErrorListener((PlayerService) context);
            mediaPlayer.setOnSeekCompleteListener((PlayerService) context);
        } else {
            mediaPlayer.reset();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startForeground() {
        if (intent == null) {
            intent = new Intent(getContext(), PlayerService.class);
        }

        SongItem songItem = (SongItem) intent.getSerializableExtra(SONG_ITEM);

        // Start foreground service.
        try {
            ((PlayerService) getContext()).startForeground(NOTIFICATION_ID, PlayerNotification.notify(getContext(), songItem));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        KeyEvent key = MediaButtonReceiver.handleIntent(getMediaSession(), intent);
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createContext();
        createMediaSession(this);
        createPlayer(this);
    }

    private void createContext() {
        if (context == null) {
            context = this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            PlayerService.intent = intent;
            String action = intent.getAction();

            switch (action) {
                case ACTION_START_PLAYER_SERVICE:
                    startForeground();
                    break;
                case ACTION_STOP_PLAYER_SERVICE:
                    stopForegroundService();
                    break;
                case ACTION_PREVIOUS:
                    callback.onSkipToPrevious();
                    break;
                case ACTION_NEXT:
                    callback.onSkipToNext();
                    break;
                case ACTION_STOP:
                    callback.onStop();
                    break;
                case ACTION_RESUME:
                    getMediaPlayer().start();
                    getMediaPlayer().seekTo(currentPosition);
                    break;
                case ACTION_SEEKTO:
                    //getMediaSession().setPlaybackState();
                    break;
                case ACTION_PAUSE:
                    pauseSong();
                    break;
                case ACTION_PLAY:
                    playSong();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void pauseSong() {
        callback.onPause();
    }

    private void playSong() {
        SongItem songItem = (SongItem) intent.getSerializableExtra(SONG_ITEM);
        createMediaSession(getContext());
        getMediaSession().setActive(true);
        MediaPlayer.create(getContext(), songItem.getUri());

        if (getMediaPlayer().isPlaying())
            callback.onStop();
        else {
            createPlayer(getContext());
            try {
                getMediaPlayer().setDataSource(getContext(), songItem.getUri());
                getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
                getMediaPlayer().prepare();
                callback.onPlay();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Intent stopServiceIntent = new Intent(this, PlayerService.class);
        stopServiceIntent.setAction(PlayerService.ACTION_STOP_PLAYER_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, stopServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        callback.onPause();
    }
}
