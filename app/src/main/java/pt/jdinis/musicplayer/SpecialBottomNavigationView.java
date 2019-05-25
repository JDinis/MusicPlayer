package pt.jdinis.musicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpecialBottomNavigationView extends BottomNavigationView {

    public SpecialBottomNavigationView(Context context) {
        super(context);

        this.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Constants.SelectedTab = menuItem.getItemId();
                return true;
            }
        });
    }

    public SpecialBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Constants.SelectedTab = menuItem.getItemId();
                return true;
            }
        });
    }

    public SpecialBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Constants.SelectedTab = menuItem.getItemId();
                return true;
            }
        });
    }

    public void SelectTab(Context context, int id) {
        int fragmentId = 0;
        Fragment newFragment = null;

        switch (id) {
            case R.id.menu_item_home:
                fragmentId = R.layout.fragment_home;
                newFragment = new Home();
                break;
            case R.id.menu_item_playlists:
                fragmentId = R.layout.fragment_playlists;
                newFragment = new Playlists();
                break;
        }
        FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();

        transaction.replace(fragmentId, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }
}
