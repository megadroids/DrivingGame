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
 * Created by megadroids.
 */

public class JSONWriter {


    public void JSONWrite(Context context, int highscore, int point, ArrayList<String> cars, ArrayList<String> themes) throws JSONException {

        //Json file layout
        //[{"highscore":222,"points":20,"cars":["01","02","03"],"themes":["christmas.png","farm.png","city.png"]}]

        //load the existing file content to update
        JSONReader reader = new JSONReader();
        JSONArray value = reader.load(context);

        //JSONArray arr = new JSONArray();

        //JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj ;
        //when there are no scores in the file its time application run
        if(value == null){
            value = new JSONArray();
            jsonObj = new JSONObject();
        }
        else {
            jsonObj = (JSONObject) value.get(0);
        }

        //update only if the correct value has been pased
        if(highscore != -1) {
            jsonObj.put("highscore", highscore);
        }
        if(point !=-1) {
            jsonObj.put("points", point);
        }

        if(cars != null) {
            JSONArray carArr = new JSONArray();
            for (int i = 0; i < cars.size(); i++) {
                carArr.put(cars.get(i));
            }
            jsonObj.put("cars", carArr);
        }


        if(themes != null) {
            JSONArray themeArr = new JSONArray();
            for (int i = 0; i < themes.size(); i++) {
                themeArr.put(themes.get(i));
            }
            jsonObj.put("themes", themeArr);
        }

        value.put(jsonObj);

        try {
            FileOutputStream fos = context.getApplicationContext().openFileOutput("UserData.json", context.MODE_PRIVATE);
            fos.write(value.toString().getBytes());
            fos.close();

        } catch (IOException e) {
            Log.e("JSONWriteException",e.getMessage());
        }

    }
}
