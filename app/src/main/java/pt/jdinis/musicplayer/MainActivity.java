package pt.jdinis.musicplayer;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static androidx.fragment.app.FragmentManager fragmentManager;
    private static ViewPager viewPager;
    private static View mainView;

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
                    Home.getBottomNavigationView().setSelectedItemId(Constants.getSelectedMenuID(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        return viewPager;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /*ActionBar supportActionBar = null;

        try {
            supportActionBar = getSupportActionBar();
        } catch (NullPointerException e) {
            Log.e(Constants.MAINACTIVITY_TAG, "Failed to display logo on title bar.");
        }

        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setLogo(R.mipmap.ic_launcher);
            supportActionBar.setDisplayUseLogoEnabled(true);
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setLogo(R.mipmap.ic_launcher);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (mainView == null)
            mainView = findViewById(android.R.id.content);

        if (getViewPager().getAdapter() == null)
            getViewPager().setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));

        fragmentManager = getSupportFragmentManager();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getViewPager().setCurrentItem(0);
        } else if (id == R.id.nav_playlist) {
            getViewPager().setCurrentItem(1);
        } else if (id == R.id.nav_favorites) {
            getViewPager().setCurrentItem(2);
        } else if (id == R.id.nav_my_music) {
            getViewPager().setCurrentItem(3);
        } else if (id == R.id.nav_settings) {
            getViewPager().setCurrentItem(4);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        getViewPager().setCurrentItem(Constants.SelectedTabID);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (getViewPager().getCurrentItem() != Constants.SelectedTabID)
            Constants.SelectedTabID = getViewPager().getCurrentItem();

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
