package si.uni_lj.fri.pbd.miniapp3.adapter;

/*
 * SECTION PAGER ADAPTER
 *
 * This adapter ensures that the right fragment is shown when tabs are switched.
 *
 */

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import si.uni_lj.fri.pbd.miniapp3.ui.favorites.FavoritesFragment;
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchFragment;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    // FIELDS
    private int numOfTabs;

    // CONSTRUCTOR
    public SectionsPagerAdapter(FragmentActivity fragment, int numOfTabs) {
        super(fragment);
        this.numOfTabs = numOfTabs;
    }

    // method for switching fragments
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new SearchFragment();
            case 1:
                return new FavoritesFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return this.numOfTabs;
    }
}
