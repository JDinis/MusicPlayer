package pt.jdinis.musicplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return Home.newInstance();
            case 1:
                return Playlists.newInstance();
            case 2:
                return Playlists.newInstance();
            case 3:
                return Playlists.newInstance();
            case 4:
                return Playlists.newInstance();
            default:
                return Home.newInstance();
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 5;
    }
}
