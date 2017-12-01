package megadroid.drivinggame.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.Purchase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private Purchase purchaser;
    private List<ImageButton> carButtons;
    private List<ImageButton> themeButtons;

    private final String FIRST_CAR_IDENTIFIER = "FirstCar";
    private final String SECOND_CAR_IDENTIFIER = "SecondCar";
    private final String FIRST_THEME_IDENTIFIER = "FirstTheme";
    private final String SECOND_THEME_IDENTIFIER = "SecondTheme";

    HashMap<String, Integer> alternativeImages;
    HashMap<Integer, String> intIdToString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        MobileAds.initialize(this, "ca-app-pub-1558090702648041~7979634477");

        monitor = new ScoreMonitor();
        carlist = new ArrayList<String>();
        themelist = new ArrayList<String>();
        alternativeImages = new HashMap<>();
        alternativeImages.put("FirstCar", R.drawable.def_car);
        alternativeImages.put("SecondCar", R.drawable.car3_unlocked);
        alternativeImages.put("ThirdCar", R.drawable.rocket_unlocked);

        intIdToString = new HashMap<>();
        intIdToString.put(R.id.firstCar, "FirstCar");
        intIdToString.put(R.id.secondCar, "SecondCar");
        intIdToString.put(R.id.thirdCar, "ThirdCar");
        intIdToString.put(R.id.fourthCar, "FourthCar");

        try {
            purchaser = new Purchase(this, "Shop");
        }catch (JSONException e){
            Toast.makeText(this, "Problem loading from the database", Toast.LENGTH_SHORT);
        }
        carButtons = new ArrayList<>();
        themeButtons = new ArrayList<>();

        initializeButtons();
    }

    private void redrawScreen() {
        TextView pointsView = (TextView) findViewById(R.id.points);
        pointsView.setText(Integer.toString(purchaser.getPoints()));

        //Setting the appropriate images for each car button item
        for (ImageButton imageButton : carButtons) {
            String carName = intIdToString.get(imageButton.getId());
            if (purchaser.carSelected(carName)) {
                imageButton.setImageResource(alternativeImages.get(carName));
                imageButton.setBackgroundResource(R.color.purple);
            }
            else if (purchaser.isCarBought(carName)) {
                imageButton.setImageResource(alternativeImages.get(carName));
                imageButton.setBackgroundResource(R.color.colorAccent);
            }
            else{
                imageButton.setBackgroundResource(R.color.colorAccent);
            }
        }
    }
    private void initializeButtons(){

        carButtons.add((ImageButton) findViewById(R.id.firstCar));
        carButtons.add((ImageButton) findViewById(R.id.secondCar));
        carButtons.add((ImageButton) findViewById(R.id.thirdCar));
        for(ImageButton imageButton : carButtons){
            imageButton.setOnClickListener(this);
        }
        //for(ImageButton imageButton : themeButtons){
        //    imageButton.setOnClickListener(this);
        //}
    }

    @Override
    protected void onStart(){
        super.onStart();

        redrawScreen();
    }

    @Override
    public void onClick(View view){

        String viewName = intIdToString.get(view.getId());

        //Code segment for car imagebutton click events
        if(purchaser.isCarBought(viewName)){
            purchaser.selectCar(viewName);
        }
        else if(purchaser.itemAffordable(viewName)){
            purchaser.purchaseCar(viewName);
            purchaser.selectCar(viewName);
        }
        //Code segment for Theme imagebutton click events
        else if(purchaser.isThemeBought(viewName)){
            purchaser.selectTheme(viewName);
        }
        else if(purchaser.itemAffordable(viewName)){
            purchaser.purchaseTheme(viewName);
            purchaser.selectTheme(viewName);
        }
        else{
            //Intent ad = new Intent(this, advActivity.class);
            //todo CALL JOAO's advert
        }
        redrawScreen();
    }

    /**
     * Overriden method for when the user leaves the shopactivity,
     * will save all changes made during the users shopping activity to
     * the json database
     */
    @Override
    public void onPause(){
        super.onPause();
        try {
            purchaser.closeShop(this);
        }catch(JSONException e){
            Toast.makeText(this, "Problem saving to the database", Toast.LENGTH_SHORT);
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
}