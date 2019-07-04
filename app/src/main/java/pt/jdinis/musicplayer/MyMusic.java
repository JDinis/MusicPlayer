package pt.jdinis.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MyMusic extends Fragment {
    public static MyMusic newInstance() {
        return new MyMusic();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_my_music, container, false);
    }
}
