package megadroid.drivinggame.controller;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;

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
        //todo: replace def_Car with string playerCar-
        selectedCar = resources.getIdentifier(playerCar, "drawable",
                context.getPackageName());

        String themeVal = monitor.getCurrentTheme();
        //todo: replace backgroundcanvas with string themeVal
        selectedTheme = resources.getIdentifier(themeVal, "drawable",
                context.getPackageName());


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
}
