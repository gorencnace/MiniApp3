package si.uni_lj.fri.pbd.miniapp3.ui;

/*
 * SPLASH ACTIVITY
 *
 * This activity wastes user's time.
 *
 * Based on https://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import si.uni_lj.fri.pbd.miniapp3.R;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
