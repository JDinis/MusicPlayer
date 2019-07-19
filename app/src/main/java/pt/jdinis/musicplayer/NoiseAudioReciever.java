package pt.jdinis.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.media.session.PlaybackStateCompat;

public class NoiseAudioReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            // Pause the playback
            PlayerService.getMediaSession().setPlaybackState(PlaybackStateCompat.fromPlaybackState(PlaybackStateCompat.STATE_PAUSED));
        } else if (AudioManager.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
            switch (intent.getIntExtra("state", -1)) {
                case 0:
                    // Unplugged
                    PlayerService.getMediaSession().setPlaybackState(PlaybackStateCompat.fromPlaybackState(PlaybackStateCompat.STATE_PAUSED));
                    break;
                case 1:
                    // Plugged
                    PlayerService.getMediaSession().setPlaybackState(PlaybackStateCompat.fromPlaybackState(PlaybackStateCompat.STATE_PLAYING));
                    break;
            }
        }
    }
}
