package megadroid.drivinggame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import megadroid.drivinggame.R;
import megadroid.drivinggame.model.SoundHelper;

/**
 * Created by Pemi on 2017-12-06.
 */

public class GameOverActivity extends AppCompatActivity {

    private int muteFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gameover);

        BackToMenu backToMenu = new BackToMenu();
        backToMenu.start();
        Intent intent = getIntent();
        boolean value = intent.getBooleanExtra("highscorebeaten",true);
        muteFlag = intent.getIntExtra("muteFlag",0);

        SoundHelper msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer3(this,R.raw.game_over);
        if(muteFlag == 0) {
            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }

        if (value) {
            //ImageView imageView = (ImageView) findViewById(R.id.gameoverimage);
            //imageView.setImageResource(R.drawable.gameover_beatenscore);
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.gameoverlayout);
            constraintLayout.setBackgroundResource(R.drawable.gameover_beatenscore);
        }
        else {
            //ImageView imageView = (ImageView) findViewById(R.id.gameoverimage);
            //imageView.setImageResource(R.drawable.gameover_notbeatenscore);
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.gameoverlayout);
            constraintLayout.setBackgroundResource(R.drawable.gameover_notbeatenscore);
        }
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


}
