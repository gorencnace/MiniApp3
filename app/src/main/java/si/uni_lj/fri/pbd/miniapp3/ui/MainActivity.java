package si.uni_lj.fri.pbd.miniapp3.ui;
/*
 * MAIN ACTIVITY
 *
 * Main Activity sets up Toolbar.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import si.uni_lj.fri.pbd.miniapp3.Constants;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configureTabLayout();
    }

    // toolbar setup (from lab4)
    private void configureTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.viewpager);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(MainActivity.this, Constants.NUM_OF_TABS);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch(position) {
                            case 0:
                                tab.setText(R.string.search_tab);
                                break;
                            case 1:
                                tab.setText(R.string.favorite_tab);
                                break;
                        }
                    }
                }).attach();
    }
}
