package pt.jdinis.musicplayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class SongItem implements Serializable {
    private transient MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private String Album, Artist, Title, Duration, UriPath;

    public SongItem(String url, @Nullable Context context) {
        if (url.contains("http")) {
            mmr.setDataSource(url);
        } else {
            mmr.setDataSource(context, Uri.parse(url));
        }

        Album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        Artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        Title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        Duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        UriPath = url;
    }

    public String getAlbum() {
        return Album;
    }

    public String getArtist() {
        return Artist;
    }

    public String getTitle() {
        return Title;
    }

    public String getDuration() {
        return Duration;
    }

    public Uri getUri() {
        return Uri.parse(UriPath);
    }

    public String getUriPath() {
        return UriPath;
    }

    public Bitmap getAlbumImage(Resources resources) {
        if (mmr == null) {
            mmr = new MediaMetadataRetriever();
        }

        mmr.setDataSource(getUriPath());

        byte[] albumBytes = new byte[0];

        try {
            albumBytes = mmr.getEmbeddedPicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (albumBytes == null) {
            return BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
        }

        Bitmap albumBmp = BitmapFactory.decodeByteArray(albumBytes, 0, albumBytes.length);
        return albumBmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.equals(this)) {
            SongItem songItem = (SongItem) obj;
            return songItem.Artist == this.Artist && songItem.Album == this.Album && songItem.Duration == this.Duration && songItem.Title == this.Title;
        }
        return false;
    }
}
