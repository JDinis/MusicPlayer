package pt.jdinis.musicplayer;

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
                return 3;
            case R.id.menu_item_settings:
                return 4;
            default:
                return 0;
        }
    }
}
