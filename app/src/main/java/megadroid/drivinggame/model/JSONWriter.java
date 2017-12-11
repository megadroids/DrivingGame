package megadroid.drivinggame.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to write the user specific data to the JSON file
 */

public class JSONWriter {

    /**
     * Constructor used to get the user data and write to JSON file
     * @param context
     * @param highscore - high score achieved by the user
     * @param point - the items collected by the user
     * @param cars - the cars bought by the user
     * @param themes - the themes bought by the user
     * @param currentCar - the current car selected by the user
     * @param currentTheme - the current theme selected by the user
     * @throws JSONException - the exception encountered during the file write operation
     */
    public void JSONWrite(Context context, int highscore, int point, ArrayList<String> cars, ArrayList<String> themes,String currentCar, String currentTheme) throws JSONException {

        //Json file layout
        //[{"highscore":222,"points":20,"cars":["01","02","03"],"themes":["christmas.png","farm.png","city.png"],
        // "currentcar":"01","currenttheme":"city.png"}]

        //load the existing file content to update
        JSONReader reader = new JSONReader();
        JSONArray value = reader.load(context);

        JSONArray arr = new JSONArray();

        //JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj ;
        JSONArray carArr;
        JSONArray themeArr;
        //when there are no scores in the file its time application run
        if(value == null){

            jsonObj = new JSONObject();
            carArr = new JSONArray();
            themeArr = new JSONArray();
        }
        else {
            jsonObj = (JSONObject) value.get(0);
            carArr = new JSONArray();
            themeArr = new JSONArray();
        }

        //update only if the correct value has been passed
        if(highscore != -1) {
            jsonObj.put("highscore", highscore);
        }
        if(point !=-1) {
            jsonObj.put("points", point);
        }

        if(cars != null) {
            //JSONArray carArr = new JSONArray();
            for (int i = 0; i < cars.size(); i++) {
                carArr.put(cars.get(i));
            }
            jsonObj.put("cars", carArr);
        }


        if(themes != null) {
            //JSONArray themeArr = new JSONArray();
            for (int i = 0; i < themes.size(); i++) {
                themeArr.put(themes.get(i));
            }
            jsonObj.put("themes", themeArr);
        }

        if(currentCar!=null){
            jsonObj.put("currentcar", currentCar);
        }

        if(currentTheme!=null){
            jsonObj.put("currenttheme", currentTheme);
        }


        arr.put(jsonObj);

        try {
            FileOutputStream fos = context.getApplicationContext().openFileOutput("UserData.json", context.MODE_PRIVATE);
            fos.write(arr.toString().getBytes());
            fos.close();

        } catch (IOException e) {
            Log.e("JSONWriteException",e.getMessage());
        }

    }
}
