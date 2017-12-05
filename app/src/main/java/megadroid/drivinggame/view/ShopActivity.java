package megadroid.drivinggame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;

import java.util.ArrayList;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import megadroid.drivinggame.model.SoundHelper;

public class ShopActivity extends AppCompatActivity{

    private int points;
    private ScoreMonitor monitor;
    private ArrayList<String> carlist;
    private ArrayList<String> themelist;
    private String currentCar;
    private String currentTheme;
    private SoundHelper msoundHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        MobileAds.initialize(this, "ca-app-pub-1558090702648041~7979634477");

        monitor = new ScoreMonitor();
        carlist = new ArrayList<String>();
        themelist = new ArrayList<String>();

        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer(this,R.raw.simple_game_music);
        msoundHelper.playMusic();



        //read points, cars and themes from JSON file
        readJson();

        //toDo: cars , themes and updated points should be written from the method of buy button click
        //toDo: but highscore should not be updated from ShopActivity so pass -1 as given below
        int highscore = -1;

       highscore = 0;
        points = 0;
        //ArrayList<String> cars = new ArrayList<String>(); //{"01", "02", "03"};
        carlist.add("def_car");
        carlist.add("02");
        carlist.add("03");

        //ArrayList<String> themes = new ArrayList<String>(); //{"christmas.png", "farm.png", "city.png"};
        themelist.add("backgroundcanvas");
        themelist.add("bg2");
        themelist.add("city.png");

        currentCar="rocket_unlocked";
        currentTheme="bg2";


        try {
            monitor.writeJSON(this, highscore, points, carlist, themelist, currentCar,currentTheme);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void readJson() {
        //read scores from JSON file
        try {

            String values = monitor.readJSON(this.getApplicationContext(),"Shop");

            if (values.isEmpty()) {
                Toast.makeText(this, "No Scores", Toast.LENGTH_LONG).show();
            } else {

                points = monitor.getPoints();
                carlist = monitor.getCarlist();
                themelist = monitor.getThemelist();
                currentCar =monitor.getCurrentCar();
                currentTheme = monitor.getCurrentTheme();
            }

            //Toast.makeText(this,textToPrint,Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }



    protected void onClick(View view){

        boolean watchAd = false;

        //logic to set watchAd true
        watchAd = true;

        if(watchAd){
            Intent myIntent = new Intent(ShopActivity.this, AdvActivity.class);
            ShopActivity.this.startActivity(myIntent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        msoundHelper.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        msoundHelper.playMusic();
    }
}



