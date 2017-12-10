package megadroid.drivinggame.view;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import megadroid.drivinggame.model.SoundHelper;

public class GameActivity extends AppCompatActivity  {


    //declaring gameview
    private GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        Intent intent = getIntent();
        int muteFlag = intent.getIntExtra("muteFlag",0); //if it's a string you stored.


        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor
        gameView = new GameView(this, size.x, size.y, muteFlag);

        //hide the bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //adding it to contentview
        setContentView(gameView);

    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {

        super.onPause();

        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();

    }

    @Override
    public void onBackPressed() {
    }

}
