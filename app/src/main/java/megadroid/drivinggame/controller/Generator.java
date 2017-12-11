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
 * Class used to generate the random logic for the obstacles required in the game
 */

public class Generator {
    private ScoreMonitor monitor;
    private int highScore;
    private Resources resources ;
    private int points;
    private final int selectedTheme;
    private final int selectedCar;

    private String themeVal;
    private ArrayList<Integer> randomObstacleCars;
    private ArrayList<Integer> randommusic;
    private ArrayList<Integer> randomObstacleSpace;

    /**
     * Constructor method used to initialise the obstacles adn also to read the values from the JSON
     * @param context
     */
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

        //random music generation
        randommusic  = new ArrayList<Integer>(3);
        randommusic.add(R.raw.main_game1);
        randommusic.add(R.raw.main_game2);
        randommusic.add(R.raw.main_game3);
        Collections.shuffle(randommusic);

        //random obstacle car generation
        randomObstacleCars = new ArrayList<Integer>(7);
        randomObstacleCars.add(R.drawable.car_obst_0);
        randomObstacleCars.add(R.drawable.car_obst_1);
        randomObstacleCars.add(R.drawable.car_obst_2);
        randomObstacleCars.add(R.drawable.car_obst_3);
        randomObstacleCars.add(R.drawable.car_obst_4);
        randomObstacleCars.add(R.drawable.car_obst_5);
        randomObstacleCars.add(R.drawable.car_obst_6);
        Collections.shuffle(randomObstacleCars);

        //random space obstacle generation
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

    //Method to get the current theme name
    public String getThemeVal() {
        return themeVal;
    }
    //Method to get the current theme image resource ID
    public int getSelectedTheme() {
        return selectedTheme;
    }
    //Method to get the highscore value for the player
    public int getHighScore() {
        return highScore;
    }
    //Method to get the points for the player
    public int getPoints(){ return points; }
    //Method to get the resource ID corresponding to the car selected by the user
    public int getSelectedCar() {
        return selectedCar;
    }


    //write the score to Json when exiting the screen
    public void writeJson(Context context,int newhighscore,int points) {

        try {
            monitor.writeJSON(context, newhighscore, points, null, null, null, null);
        } catch (JSONException e) {
            Log.e("JSONWriteException", e.getMessage());
        }
    }

    //Method to return a shuffled music
    public int randomMainMusic(int x) {
        return randommusic.get(x);
    }
    //Method to return a shuffled obstacle
    public int randomObstacleCars(int x) {
        return randomObstacleCars.get(x);
    }

    //Method to return a shuffled space obstacle
    public int randomObstacleSpace(int x) {
        return randomObstacleSpace.get(x);
    }
}
