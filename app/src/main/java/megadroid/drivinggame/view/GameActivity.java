package megadroid.drivinggame.view;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.Toast;

import org.json.JSONException;

import megadroid.drivinggame.controller.ScoreMonitor;

public class GameActivity extends AppCompatActivity {

    //declaring gameview
    private GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor
        gameView = new GameView(this, size.x, size.y);


        //adding it to contentview
        setContentView(gameView);

    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        writeJson();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }


    //write the score to Json when exiting the screen
    private void writeJson(){
        //write the score to Json File
        ScoreMonitor monitor =new ScoreMonitor();

        //toDO: get the highscore and points from gameview
        int  highscore =900;
        int  points = 2000;

        //toDo: cars , themes and updated points should be written from shopActivity, will pass null here
        // String[] cars = new String[]{"01", "02", "03"};
        //String [] themes = new String[] {"christmas.png","farm.png","city.png"};

        try {
            monitor.writeJSON(this,highscore,points,null,null,null,null);
        } catch (JSONException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }




    }
/*
can be used if the Menu activity does not refersh
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

