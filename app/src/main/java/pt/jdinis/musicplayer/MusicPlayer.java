package pt.jdinis.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;

public class MusicPlayer implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener {
    private static MediaPlayer mediaPlayer;
    private static Context PlayerContext;

    public MusicPlayer(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            PlayerContext = context;

            mediaPlayer.setWakeMode(context.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
        } else {
            mediaPlayer.reset();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
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
}
