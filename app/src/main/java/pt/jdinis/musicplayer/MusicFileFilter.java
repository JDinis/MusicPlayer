package pt.jdinis.musicplayer;

import java.io.File;
import java.io.FileFilter;

public class MusicFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return checkFileExtension(pathname);
    }

    private boolean checkFileExtension(File f) {
        String ext = getFileExtension(f);
        if (ext == null) return false;
        try {
            if (SupportedFileFormat.valueOf(ext.toUpperCase()) != null) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return false;
    }

    public String getFileExtension(File f) {
        return getFileExtension(f.getName());
    }

    public String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return null;
        }
    }

    enum SupportedFileFormat {
        _3GP("3gp"),
        MP4("mp4"),
        M4A("m4a"),
        AAC("aac"),
        TS("ts"),
        FLAC("flac"),
        MP3("mp3"),
        MID("mid"),
        XMF("xmf"),
        MXMF("mxmf"),
        RTTTL("rtttl"),
        RTX("rtx"),
        OTA("ota"),
        IMY("imy"),
        OGG("ogg"),
        MKV("mkv"),
        WAV("wav");

        private String filesuffix;

        SupportedFileFormat(String filesuffix) {
            this.filesuffix = filesuffix;
        }
    }
}
