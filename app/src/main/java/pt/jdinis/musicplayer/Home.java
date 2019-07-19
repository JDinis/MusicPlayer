package pt.jdinis.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;


public class Home extends Fragment {
    private static View mainView;
    private static SpecialBottomNavigationView bottomNavigationView;
    private static RecyclerView recyclerView;

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }


    public static Home newInstance() {
        return new Home();
    }

    public static View getMainView() throws NullPointerException {
        if (mainView == null)
            throw new NullPointerException();
        return mainView;
    }


    public static SpecialBottomNavigationView getBottomNavigationView() {
        if (bottomNavigationView == null) {
            try {
                bottomNavigationView = getMainView().findViewById(R.id.bottom_navigation_view);

                return bottomNavigationView;
            } catch (NullPointerException e) {
                Log.e(Constants.MAINACTIVITY_TAG, "Failed to obtain a valid context.");
            }
        }

        bottomNavigationView.setSelectedItemId(Constants.SelectedTabID);
        return bottomNavigationView;
    }


    private static void requestPermission(Activity context) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(context, Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE},
                    1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);

        if (mainView == null)
            mainView = inflate.getRootView();

        if (bottomNavigationView == null)
            getBottomNavigationView();

        recyclerView = inflate.findViewById(R.id.recyclerView);
        ArrayList<SongItem> songItems = new ArrayList<>();

        requestPermission(getActivity());

        File[] files = Environment.getExternalStorageDirectory().listFiles();

        if (files != null)
            for (File dir : files) {
                for (File song : dir.listFiles(new MusicFileFilter())) {
                    SongItem songItem = new SongItem(song.getAbsolutePath(), getContext());
                    songItems.add(songItem);
                }
            }

        if (!Environment.isExternalStorageEmulated()) {
            files = new File("/sdcard").listFiles();

            if (files != null)
                for (File dir : files) {
                    for (File song : dir.listFiles(new MusicFileFilter())) {
                        SongItem songItem = new SongItem(song.getAbsolutePath(), getContext());
                        songItems.add(songItem);
                    }
                }
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        SongAdapter adapter = new SongAdapter(songItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //if (getViewPager().getAdapter() == null)
        //    getViewPager().setAdapter(new MyFragmentPageAdapter(((FragmentActivity)inflater.getContext()).getSupportFragmentManager()));

        return inflate;
    }

    @Override
    public void onResume() {
        //getViewPager().setCurrentItem(Constants.SelectedTabID);
        getBottomNavigationView();
        super.onResume();
    }

    @Override
    public void onPause() {
        //if (getViewPager().getCurrentItem() != Constants.SelectedTabID)
        //    Constants.SelectedTabID = getViewPager().getCurrentItem();

        super.onPause();
    }
}
