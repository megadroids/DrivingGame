package megadroid.drivinggame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import megadroid.drivinggame.R;
import megadroid.drivinggame.model.SoundHelper;

/**
 * Class used to generate the initial splash screen of the game
 */
public class SplashScreenActivity extends AppCompatActivity {
    private SoundHelper msoundHelper;

    /**
     * Method invoked when the activity is created and used to create the music and logo objects
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //play the initial sound fo the game
        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer3(this,R.raw.start);//loading_page
        msoundHelper.playMusic();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

        //hide the bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * Class used to implement wait logic for the splash screen and redirect to the Main activity
     */
    public class LogoLauncher extends Thread {
        public void run() {
            try {
                sleep(2500);                        //*****SLEEP TIMER*****
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();

        }

    }

    /**
     * Method invoked when the activity is paused
     */
    protected void onPause() {
        super.onPause();
        //stop music when going to Main activity
        msoundHelper.pauseMusic();
        msoundHelper.stopMusic();
        msoundHelper = null;

    }
}

