package megadroid.drivinggame.controller;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

import megadroid.drivinggame.model.JSONReader;
import megadroid.drivinggame.model.JSONWriter;

/**
 * Created by megadroids.
 */

public class ScoreMonitor {

    public void writeJSON(Context context, int highscore, int points, ArrayList<String> cars, ArrayList<String> themes) throws JSONException {

        JSONWriter writer = new JSONWriter();
        writer.JSONWrite(context,highscore,points,cars,themes);
    }

    public JSONArray readJSON(Context context) throws JSONException {
        JSONReader reader = new JSONReader();
        JSONArray value = reader.load(context);
        return value;
    }

}
