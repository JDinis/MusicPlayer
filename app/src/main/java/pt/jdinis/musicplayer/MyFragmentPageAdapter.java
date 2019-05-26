package pt.jdinis.musicplayer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return Home.newInstance();
            case 1:
                return Playlists.newInstance();
            case 2:
                return Favorites.newInstance();
            case 3:
                return MyMysic.newInstance();
            case 4:
                return Settings.newInstance();
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
