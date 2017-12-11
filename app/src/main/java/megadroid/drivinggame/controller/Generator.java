package megadroid.drivinggame.controller;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import megadroid.drivinggame.R;

/**
 * Created by megadroids on 11/23/2017.
 */

public class Generator {
    private ScoreMonitor monitor;
    private int highScore;
    private Resources resources ;
    private int points;
    private final int selectedTheme;
    private final int selectedCar;
    //Array to hold car obstacles
   // int[] randomObstacleCars;
    private Random random = new Random();
    private String themeVal;
    //private int[] randomObstacleCars;
    private ArrayList<Integer> randomObstacleCars;
    //private int[] randommusic;
    private ArrayList<Integer> randommusic;
    // private int[] randomObstacleSpace;
    private ArrayList<Integer> randomObstacleSpace;

    public Generator(Context context){
        monitor = new ScoreMonitor();
        //getting values from JSON
        String values = null;
        String playerCar="";
        try {
            values = monitor.readJSON(context,"Game");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!values.isEmpty()){

            highScore = monitor.getHighScore();
            playerCar = monitor.getCurrentCar()+"_game";
            points = monitor.getPoints();
        }

        resources = context.getResources();

        selectedCar = resources.getIdentifier(playerCar, "drawable",
                context.getPackageName());

        themeVal = monitor.getCurrentTheme();

        selectedTheme = resources.getIdentifier(themeVal, "drawable",
                context.getPackageName());

        //randommusic = new int[] {R.raw.main_game1, R.raw.main_game2, R.raw.main_game3};
        randommusic  = new ArrayList<Integer>(3);
        randommusic.add(R.raw.main_game1);
        randommusic.add(R.raw.main_game2);
        randommusic.add(R.raw.main_game3);
        Collections.shuffle(randommusic);

        //randomObstacleCars = new int[] {R.drawable.car_obst_0, R.drawable.car_obst_1, R.drawable.car_obst_2, R.drawable.car_obst_3, R.drawable.car_obst_4, R.drawable.car_obst_5,R.drawable.car_obst_6};
        randomObstacleCars = new ArrayList<Integer>(7);
        randomObstacleCars.add(R.drawable.car_obst_0);
        randomObstacleCars.add(R.drawable.car_obst_1);
        randomObstacleCars.add(R.drawable.car_obst_2);
        randomObstacleCars.add(R.drawable.car_obst_3);
        randomObstacleCars.add(R.drawable.car_obst_4);
        randomObstacleCars.add(R.drawable.car_obst_5);
        randomObstacleCars.add(R.drawable.car_obst_6);
        Collections.shuffle(randomObstacleCars);

        //randomObstacleSpace = new int[] {R.drawable.space_obst1, R.drawable.space_obst2, R.drawable.space_obst3, R.drawable.space_obst4, R.drawable.space_obst5, R.drawable.space_obst6,R.drawable.space_obst7,R.drawable.asteroid};
        randomObstacleSpace = new ArrayList<Integer>(8);
        randomObstacleSpace.add(R.drawable.space_obst1);
        randomObstacleSpace.add(R.drawable.space_obst2);
        randomObstacleSpace.add(R.drawable.space_obst3);
        randomObstacleSpace.add(R.drawable.space_obst4);
        randomObstacleSpace.add(R.drawable.space_obst5);
        randomObstacleSpace.add(R.drawable.space_obst6);

        randomObstacleSpace.add(R.drawable.asteroid);
        Collections.shuffle(randomObstacleSpace);
    }

    public String getThemeVal() {
        return themeVal;
    }

    public int getSelectedTheme() {
        return selectedTheme;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getPoints(){ return points; }

    public int getSelectedCar() {
        return selectedCar;
    }


    //write the score to Json when exiting the screen
    public void writeJson(Context context,int newhighscore,int points) {
        //write the score to Json File
        // int highscore = 700;
        // int points = 2000;

        //toDo: cars , themes and updated points should be written from shopActivity, will pass null here
        // String[] cars = new String[]{"01", "02", "03"};
        //String [] themes = new String[] {"christmas.png","farm.png","city.png"};

        try {
            monitor.writeJSON(context, newhighscore, points, null, null, null, null);
        } catch (JSONException e) {
            Log.e("JSONWriteException", e.getMessage());
        }
    }

    public int randomMainMusic(int x) {

        //int x = random.nextInt(randommusic.length);
        //return randommusic[x];
        return randommusic.get(x);
    }

    public int randomObstacleCars(int x) {


        //int x = random.nextInt(randomObstacleCars.length);
        //return randomObstacleCars[x];
        return randomObstacleCars.get(x);
    }

    public int randomObstacleSpace(int x) {

        //int x = random.nextInt(randomObstacleSpace.length);
        //return randomObstacleSpace[x];
        return randomObstacleSpace.get(x);
    }
}
