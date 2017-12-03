package megadroid.drivinggame.controller;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import megadroid.drivinggame.R;
import megadroid.drivinggame.model.JSONReader;
import megadroid.drivinggame.model.JSONWriter;

/**
 * Created by megadroids.
 */

public class ScoreMonitor {

    private ArrayList<String> themelist;
    private int highScore;
    private int points;
    private ArrayList<String> carlist;
    private String textToPrint ;
    private String currentCar;
    private String currentTheme;

    //method to write data into JSON
    public void writeJSON(Context context, int highscore, int points, ArrayList<String> cars, ArrayList<String> themes,String currentCar, String currentTheme) throws JSONException {

        JSONWriter writer = new JSONWriter();
        writer.JSONWrite(context,highscore,points,cars,themes,currentCar,currentTheme);
    }

    //Reading the complete Json File
    public String readJSON(Context context, String screenType) throws JSONException {

        textToPrint = "";
        JSONReader reader = new JSONReader();
        JSONArray value = reader.load(context);

        if(value != null){
            readJsonArray(value,screenType);
        }
        return textToPrint;
    }

    private void readJsonArray(JSONArray values, String screenType){
        //read scores from JSON file

        carlist = new ArrayList<String>();
        themelist = new ArrayList<String>();

        for (int i = 0; i < values.length(); i++) {
            try {
                JSONObject message = (JSONObject) values.get(i);
                highScore = message.getInt("highscore")  ;
                points =  message.getInt("points")  ;

                textToPrint += (message.get("highscore")) + "\n";
                textToPrint += (message.get("points")) + "\n";

                if(screenType.equals("Shop")) {
                    currentCar = message.getString("currentcar");
                    currentTheme = message.getString("currenttheme");

                    textToPrint += (message.getString("currentcar")) + "\n";
                    textToPrint += (message.getString("currenttheme")) + "\n";

                    JSONArray carsArr = message.getJSONArray("cars");
                    for (i = 0; i < carsArr.length(); i++) {
                        String car = (String) carsArr.get(i);

                        carlist.add(car);

                        textToPrint += car + "\n";
                    }

                    JSONArray themeArr = message.getJSONArray("themes");
                    for (i = 0; i < themeArr.length(); i++) {
                        String theme = (String) themeArr.get(i);

                        themelist.add(theme);

                        textToPrint += theme + "\n";
                    }

                }

            } catch (JSONException e) {
                Log.e("JSONReadException",e.getMessage());
            }

            }

    }


    public int getHighScore() {
        return highScore;
    }

    public int getPoints() {
        return points;
    }


    public ArrayList<String> getThemelist() {
        return themelist;
    }


    public ArrayList<String> getCarlist() {
        return carlist;
    }

    public String getCurrentCar() {
        return currentCar;
    }
    public String getCurrentTheme() {
        return currentTheme;
    }

}
