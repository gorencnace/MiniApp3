package si.uni_lj.fri.pbd.miniapp3;

/*
 * CORE APPLICATION
 *
 * This was already here and it contributed absolutely nothing.
 *
 */

import android.app.Application;
import com.amitshekhar.BuildConfig;
import timber.log.Timber;

public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        plantTimber();
    }

    private void plantTimber() {
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }

}
