package megadroid.drivinggame.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import megadroid.drivinggame.R;
import megadroid.drivinggame.model.SoundHelper;

/**
 * Created by Pemi on 2017-12-06.
 */

public class GameOverActivity extends AppCompatActivity {

    private int muteFlag;
    private SoundHelper msoundHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gameover);

        Intent intent = getIntent();
        boolean value = intent.getBooleanExtra("highscorebeaten",true);
        muteFlag = intent.getIntExtra("muteFlag",0);

        BackToMenu backToMenu = new BackToMenu();
        backToMenu.start();


        msoundHelper = new SoundHelper(this);


        if (value) {
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.gameoverlayout);
            constraintLayout.setBackgroundResource(R.drawable.gameover_beatenscore);
            msoundHelper.prepareMusicPlayer3(this, R.raw.claps);
        }
        else {
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.gameoverlayout);
            constraintLayout.setBackgroundResource(R.drawable.gameover_notbeatenscore);
            msoundHelper.prepareMusicPlayer3(this, R.raw.game_over);

        }

        if(muteFlag == 0) {

            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }

        //hide the bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public class BackToMenu extends Thread {
        public void run() {
            try {


                sleep(3500);                        //*****SLEEP TIMER*****
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
            intent.putExtra("muteFlag",muteFlag);
            startActivity(intent);
            GameOverActivity.this.finish();

        }

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop music when going to Menu activity
        msoundHelper.pauseMusic();
        msoundHelper.stopMusic();
    }
}
