package megadroid.drivinggame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import megadroid.drivinggame.R;
import megadroid.drivinggame.model.SoundHelper;

public class SplashScreenActivity extends AppCompatActivity {
    private SoundHelper msoundHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer3(this,R.raw.start);//loading_page
        msoundHelper.playMusic();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
      //  getSupportActionBar().hide();
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
    protected void onPause() {
        super.onPause();
        //stop music when going to Main activity
        msoundHelper.pauseMusic();
        msoundHelper.stopMusic();
        msoundHelper = null;

    }
}

