package megadroid.drivinggame.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //toDo: cars , themes and updated points should be written from the method of buy button click
        //toDo: but highscore should not be updated from ShopActivity so pass -1 as given below
        ScoreMonitor monitor =new ScoreMonitor();
        int highscore = -1;
        int points = 800;
        ArrayList<String> cars = new ArrayList<String>(); //{"01", "02", "03"};
        cars.add("01");
        cars.add("02");
        cars.add("03");

        ArrayList<String> themes = new ArrayList<String>(); //{"christmas.png", "farm.png", "city.png"};
        themes.add("christmas.png");
        themes.add("farm.png");
        themes.add("city.png");


        try {
            monitor.writeJSON(this,highscore,points,cars,themes);
        } catch (JSONException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
