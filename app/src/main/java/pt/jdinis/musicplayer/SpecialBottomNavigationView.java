package pt.jdinis.musicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpecialBottomNavigationView extends BottomNavigationView {

    public SpecialBottomNavigationView(final Context context) {
        super(context);

        this.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                ((ViewPager) (((MainActivity) context).findViewById(R.id.view_pager))).setCurrentItem(Constants.getSelectedTabID(menuItem.getItemId()));
                return true;
            }
        });
    }

    public SpecialBottomNavigationView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                ((ViewPager) (((MainActivity) context).findViewById(R.id.view_pager))).setCurrentItem(Constants.getSelectedTabID(menuItem.getItemId()));
                return true;
            }
        });
    }

    public SpecialBottomNavigationView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                ((ViewPager) (((MainActivity) context).findViewById(R.id.view_pager))).setCurrentItem(Constants.getSelectedTabID(menuItem.getItemId()));
                return true;
            }
        });
    }
}
