package pt.jdinis.musicplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Home extends Fragment {
    private static View mainView;
    private static SpecialBottomNavigationView bottomNavigationView;

    public static Home newInstance() {
        return new Home();
    }

    public static View getMainView() throws NullPointerException {
        if (mainView == null)
            throw new NullPointerException();
        return mainView;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);

        if (mainView == null)
            mainView = inflate.getRootView();

        if (bottomNavigationView == null)
            getBottomNavigationView();

        //if (getViewPager().getAdapter() == null)
        //    getViewPager().setAdapter(new MyFragmentPageAdapter(((FragmentActivity)inflater.getContext()).getSupportFragmentManager()));

        return inflate;
    }

    @Override
    public void onResume() {
        //getViewPager().setCurrentItem(Constants.SelectedTabID);
        getBottomNavigationView();
        super.onResume();
    }

    @Override
    public void onPause() {
        //if (getViewPager().getCurrentItem() != Constants.SelectedTabID)
        //    Constants.SelectedTabID = getViewPager().getCurrentItem();

        super.onPause();
    }
}
