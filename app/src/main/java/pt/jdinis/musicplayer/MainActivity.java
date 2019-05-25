package pt.jdinis.musicplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static SpecialBottomNavigationView bottomNavigationView;
    private View mainView;

    public View getMainView() throws NullPointerException {
        if (mainView == null)
            throw new NullPointerException();
        return mainView;
    }

    public SpecialBottomNavigationView getBottomNavigationView() {
        if (Constants.SelectedTab == -1) {
            Constants.SelectedTab = R.id.menu_item_home;
        }

        if (bottomNavigationView == null) {
            try {
                bottomNavigationView = getMainView().findViewById(R.id.bottom_navigation_view);

                return bottomNavigationView;
            } catch (NullPointerException e) {
                Log.e(Constants.MAINACTIVITY_TAG, "Failed to obtain a valid context.");
            }
        }

        bottomNavigationView.setSelectedItemId(Constants.SelectedTab);
        return bottomNavigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar supportActionBar = null;

        try {
            supportActionBar = getSupportActionBar();
        } catch (NullPointerException e) {
            Log.e(Constants.MAINACTIVITY_TAG, "Failed to display logo on title bar.");
        }

        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setLogo(R.mipmap.ic_launcher);
            supportActionBar.setDisplayUseLogoEnabled(true);
        }

        mainView = findViewById(android.R.id.content);

        if (bottomNavigationView == null)
            getBottomNavigationView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.SELECTED_TAB, Constants.SelectedTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Constants.SelectedTab = savedInstanceState.getInt(Constants.SELECTED_TAB);
        getBottomNavigationView().setSelectedItemId(Constants.SelectedTab);
    }
}
