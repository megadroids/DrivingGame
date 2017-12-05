package megadroid.drivinggame.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import org.json.JSONException;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import megadroid.drivinggame.model.SoundHelper;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundHelper msoundHelper;

    private Purchase purchaser;
    private List<ImageButton> carButtons;
    private List<ImageButton> themeButtons;

    HashMap<String, Integer> alternativeImages;
    HashMap<Integer, String> intIdToString;

    /**
     * initializes the adSense
     * creates two hashmaps, one for storing the image without the pricetag(as the default is with a pricetag),
     * the other is responsible for converting the int Id to a string for easier checking mechanics
     * <p>
     * lastly makes a controller for the activity of class Purchase.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        MobileAds.initialize(this, "ca-app-pub-1558090702648041~7979634477");

        alternativeImages = new HashMap<>();
        alternativeImages.put("def_car", R.drawable.def_car);
        alternativeImages.put("car3", R.drawable.car3_unlocked);
        alternativeImages.put("rocket", R.drawable.rocket_unlocked);
        //add as many of these as there are images with the price tag

        intIdToString = new HashMap<>();
        intIdToString.put(R.id.firstCar, "def_car");
        intIdToString.put(R.id.secondCar, "car3");
        intIdToString.put(R.id.thirdCar, "rocket");
        //  intIdToString.put(R.id.fourthCar, "FourthCar");
        //intIdToString.put();              Add as many of these for each car or theme

        try {
            purchaser = new Purchase(this, "Shop");
        } catch (JSONException e) {
            Toast.makeText(this, "Problem loading from the database", Toast.LENGTH_SHORT);
        }
        initializeButtons();

        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer(this, R.raw.simple_game_music);
        msoundHelper.playMusic();
    }

    /**
     * Will check if the item in the button images is purchased, selected or locked
     * and set the appropriate image
     * (eg. highlighed if selected, without pricetag if bought and with pricetag if locked)
     */
    private void redrawScreen() {
        TextView pointsView = (TextView) findViewById(R.id.points);
        pointsView.setText(Integer.toString(purchaser.getPoints()));
        //Setting the appropriate images for each car button item
        for (ImageButton imageButton : carButtons) {
            String carName = intIdToString.get(imageButton.getId());
            if (purchaser.carSelected(carName)) {
                imageButton.setImageResource(alternativeImages.get(carName));
                imageButton.setBackgroundResource(R.drawable.shop_frame3);
            } else if (purchaser.isCarBought(carName)) {
                imageButton.setImageResource(alternativeImages.get(carName));
                imageButton.setBackgroundResource(android.R.color.transparent);
            } else {
                imageButton.setBackgroundResource(android.R.color.transparent); // todo find out how to remove background image
            }
        }
    }

    /**
     * Will find all the button id's and add them to an array list of imagebuttons
     * so that it may be easier to call, it will also set their onCLickListeners.
     */
    private void initializeButtons() {

        carButtons = new ArrayList<>();
        themeButtons = new ArrayList<>();

        carButtons.add((ImageButton) findViewById(R.id.firstCar));
        carButtons.add((ImageButton) findViewById(R.id.secondCar));
        carButtons.add((ImageButton) findViewById(R.id.thirdCar));

        for (ImageButton imageButton : carButtons) {
            imageButton.setOnClickListener(this);
        }
        //for(ImageButton imageButton : themeButtons){
        //    imageButton.setOnClickListener(this);
        //}
    }

    /**
     * will refresh the page if the page has started or restarted
     * by calling the redrawScreen method
     */
    @Override
    protected void onStart() {
        super.onStart();

        redrawScreen();
    }

    /**
     * The overriden method for clicking on an item,
     * if the item is already bought it becomes highlighted
     * if the item isn't bought but is affordable then it will be purchased and highlighted
     * else a dialog will prompt the user if they want to watch an advert for points or not.
     *
     * @param view, the image button that has been pressed.
     */
    @Override
    public void onClick(View view) {

        String viewName = intIdToString.get(view.getId());

        //Code segment for car imagebutton click events
        if (purchaser.isCarBought(viewName)) {
            purchaser.selectCar(viewName);
        } else if (purchaser.carAffordable(viewName)) {
            purchaser.purchaseCar(viewName);
            purchaser.selectCar(viewName);
        }
        //Code segment for Theme imagebutton click events
        else if (purchaser.isThemeBought(viewName)) {
            purchaser.selectTheme(viewName);
        } else if (purchaser.themeAffordable(viewName)) {
            purchaser.purchaseTheme(viewName);
            purchaser.selectTheme(viewName);
        } else {
            //Intent ad = new Intent(this, advActivity.class);
            //todo CALL JOAO's advert
            //todo check for point increment logic
            Intent myIntent = new Intent(ShopActivity.this, AdvActivity.class);
            ShopActivity.this.startActivity(myIntent);
        }
        redrawScreen();
    }

    /**
     * Overriden method for when the user leaves the shopactivity,
     * will save all changes made during the users shopping activity to
     * the json database
     */
    @Override
    public void onPause() {
        super.onPause();
        msoundHelper.pauseMusic();
        try {
            purchaser.closeShop(this);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem saving to the database", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        msoundHelper.playMusic();
    }
}