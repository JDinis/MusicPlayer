package pt.jdinis.musicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpecialBottomNavigationView extends BottomNavigationView {

    public SpecialBottomNavigationView(final Context context) {
        super(context);
        setOnNavigationItemSelectedListener(context);
    }

    public SpecialBottomNavigationView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnNavigationItemSelectedListener(context);
    }

    public SpecialBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnNavigationItemSelectedListener(context);
    }

    private void setOnNavigationItemSelectedListener(final Context context) {
        super.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                MainActivity.getViewPager().setCurrentItem(Constants.getSelectedFragmentID(menuItem.getItemId()));
                return true;
            }
        });
    }
}
