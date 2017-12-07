package megadroid.drivinggame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import megadroid.drivinggame.R;

/**
 * Created by Pemi on 2017-12-06.
 */

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        BackToMenu backToMenu = new BackToMenu();
        backToMenu.start();
        Intent intent = getIntent();
        boolean value = intent.getBooleanExtra("highscorebeaten",true);
        if (value) {
            ImageView imageView = (ImageView) findViewById(R.id.gameoverimage);
            imageView.setImageResource(R.drawable.gameover_beatenscore);
        }
        else {
            ImageView imageView = (ImageView) findViewById(R.id.gameoverimage);
            imageView.setImageResource(R.drawable.gameover_notbeatenscore);
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
            startActivity(intent);
            GameOverActivity.this.finish();

        }

    }

}
