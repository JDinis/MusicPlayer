package pt.jdinis.musicplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class MediaButtonReceiver extends androidx.media.session.MediaButtonReceiver {
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
