package pt.jdinis.musicplayer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {
    private static SpecialBottomNavigationView bottomNavigationView;
    private static ViewPager viewPager;
    private static View mainView;
    private static boolean firstRun = true;

    public static View getMainView() throws NullPointerException {
        if (mainView == null)
            throw new NullPointerException();
        return mainView;
    }

    public static ViewPager getViewPager() {
        if (viewPager == null) {
            viewPager = getMainView().findViewById(R.id.view_pager);

            // Stops page swipes - Future Option
            //viewPager.beginFakeDrag();

            viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    // Crossfade according to guidelines: https://developer.android.com/training/animation/reveal-or-hide-view
                    page.setAlpha(0f);
                    page.setVisibility(View.VISIBLE);

                    // Start Animation for a short period of time
                    page.animate()
                            .alpha(1f)
                            .setDuration(page.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            });

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    bottomNavigationView.setSelectedItemId(Constants.getSelectedMenuID(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        return viewPager;
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

        if (getViewPager().getAdapter() == null)
            getViewPager().setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        getViewPager().setCurrentItem(Constants.SelectedTabID);
        getBottomNavigationView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (getViewPager().getCurrentItem() != Constants.SelectedTabID)
            Constants.SelectedTabID = getViewPager().getCurrentItem();

        super.onPause();
    }
}
