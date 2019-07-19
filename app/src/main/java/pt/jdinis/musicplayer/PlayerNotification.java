package pt.jdinis.musicplayer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

/**
 * Helper class for showing and canceling player
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class PlayerNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "MusicPlayer";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Notification notify(final Context context, SongItem item) throws NullPointerException {


        createNotificationChannel(context);

        NotificationCompat.Builder builder = null;

        Intent musicPlayerActivityIntent = new Intent(context, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, musicPlayerActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        try {
            builder = new NotificationCompat.Builder(context, NOTIFICATION_TAG)
                    // Show controls on lock screen even when user hides sensitive content.
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.ic_notification)

                    // Add media control buttons that invoke intents in your media service
                    .addAction(prevPendingIntent(context)) // #0
                    .addAction(pausePendingIntent(context)) // #1
                    .addAction(nextPendingIntent(context)) // #2

                    // Allows launch of activity on click
                    .setContentIntent(pendingIntent)

                    // Stop the service when the notification is swiped away
                    .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                            PlaybackStateCompat.ACTION_STOP))

                    .setContentTitle(item.getTitle())
                    .setContentText(item.getArtist())
                    .setSubText(item.getAlbum())
                    .setLargeIcon(item.getAlbumImage(context.getResources()))
                    .setNumber(1)

                    // Apply the media style template
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2 /* All */)
                            .setMediaSession(PlayerService.getMediaSession().getSessionToken())

                            // Add a cancel button
                            .setShowCancelButton(true)
                            .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                                    PlaybackStateCompat.ACTION_STOP)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context) {
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_TAG, NOTIFICATION_TAG, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.argb(255, 255, 165, 0));
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        chan.enableLights(true);
        NotificationManager service = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationCompat.Action prevPendingIntent(Context context) {
        return new NotificationCompat.Action(R.drawable.ic_prev, "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationCompat.Action nextPendingIntent(Context context) {
        return new NotificationCompat.Action(R.drawable.ic_next, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationCompat.Action pausePendingIntent(Context context) {
        if (PlayerService.getMediaPlayer() != null && PlayerService.getMediaPlayer().isPlaying())
            return new NotificationCompat.Action(
                    R.drawable.ic_pause, "Pause",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                            PlaybackStateCompat.ACTION_PAUSE));
        return new NotificationCompat.Action(
                R.drawable.ic_play, "Play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_PLAY));

    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, Notification)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
