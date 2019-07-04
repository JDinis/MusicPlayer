package pt.jdinis.musicplayer;

import android.view.View;

class Constants {
    static final String SELECTED_TAB = "SELECTED_TAB";
    static final String MAINACTIVITY_TAG = "MainActivity";
    static int SelectedTabID = 0;

    public static int getSelectedFragmentID(int selectedMenuID) {
        switch (selectedMenuID) {
            case R.id.menu_item_home:
                return 0;
            case R.id.menu_item_playlists:
                return 1;
            case R.id.menu_item_favorites:
                return 2;
            case R.id.menu_item_mymusic:
                MainActivity.getBottomNavigationView().setVisibility(View.GONE);
                return 3;
            case R.id.menu_item_settings:
                return 4;
            default:
                return 0;
        }
    }

    public static int getSelectedMenuID(int selectedFragmentID) {
        switch (selectedFragmentID) {
            case 0:
                return R.id.menu_item_home;
            case 1:
                return R.id.menu_item_playlists;
            case 2:
                return R.id.menu_item_favorites;
            case 3:
                return R.id.menu_item_mymusic;
            case 4:
                return R.id.menu_item_settings;
            default:
                return R.id.menu_item_home;
        }
    }
}
