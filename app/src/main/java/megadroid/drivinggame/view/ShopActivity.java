package megadroid.drivinggame.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.Purchase;

import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;

import megadroid.drivinggame.model.SoundHelper;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundHelper msoundHelper;

    ImageButton extraPoints;
    private Purchase purchaser;
    private List<ImageButton> carButtons;
    private List<ImageButton> themeButtons;
    private int muteFlag;

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

        //set the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //uncommetn and potentiallt rename the button id's and their corresponding string name
        //uncertain if the default car has to be there as there is no alternative image,
        // as there is no price on the original image
        //This is so that the image without the pricetag can be presented corresponding to the strings found in the json database
        alternativeImages = new HashMap<>();
        alternativeImages.put("def_car", R.drawable.def_car);
        alternativeImages.put("car_black", R.drawable.car_black);
        alternativeImages.put("rocket", R.drawable.rocket_locked);
        //alternativeImages.put("car_truck", R.drawable.car_truck);
        //alternativeImages.put("car_red", R.drawable.car_red);
        //alternativeImages.put("car_white", R.drawable.car_white);
        // alternativeImages.put("backgroundcanvas", R.drawable.backgroundcanvas);
        //alternativeImages.put("spacecanvas", R.drawable.spacecanvas);
        //add as many of these as there are images with the price tag

        //This is so that the button can have a string Id corresponding to json database
        //uncomment and potenitaly rename the button id's an their corresponding string name
        intIdToString = new HashMap<>();
        intIdToString.put(R.id.imageButton, "def_car");
        intIdToString.put(R.id.imageButton3, "car_black");
        intIdToString.put(R.id.imageButton2, "rocket");
        // intIdToString.put(R.id.truckCar, "car_truck");
        // intIdToString.put(R.id.car_red, "car_red");
        // intIdToString.put(R.id.car_white, "car_white");
        // intIdToString.put(R.id.backgroundcanvas, "backgroundcanvas");
        // intIdToString.put(R.id.spacecanvas, "spacecanvas");
        //Add as many of these for each car or theme

        try {
            purchaser = new Purchase(this, "Shop");
        } catch (JSONException e) {
            Toast.makeText(this, "Problem loading from the database", Toast.LENGTH_SHORT);
        }
        initializeButtons();

        Intent intent = getIntent();
        muteFlag = intent.getIntExtra("muteFlag", 0); //if it's a string you stored.

        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer(this, R.raw.simple_game_music);
        if (muteFlag == 0) {
            msoundHelper.playMusic();
        } else {
            msoundHelper.pauseMusic();
        }

        extraPoints = ((ImageButton) findViewById(R.id.extrapoints));
    }

    /**
     * Will check if the item in the button images is purchased, selected or locked
     * and set the appropriate image
     * (eg. highlighed if selected, without pricetag if bought and with pricetag if locked)
     */
    private void redrawScreen() {
        TextView pointsView = (TextView) findViewById(R.id.title_text_view);

        pointsView.setText(Integer.toString(purchaser.getPoints()));
        //Setting the appropriate images for each car button item
        for (ImageButton imageButton : carButtons) {
            String carName = intIdToString.get(imageButton.getId());

            if (purchaser.carSelected(carName)) {
                imageButton.setImageResource(alternativeImages.get(carName));
                imageButton.setBackgroundResource(R.drawable.shop_frame3);
            }
            else if (purchaser.isCarBought(carName)) {
                imageButton.setImageResource(alternativeImages.get(carName));
                imageButton.setBackgroundResource(android.R.color.transparent);
            }
            else {
                imageButton.setBackgroundResource(android.R.color.transparent); // todo find out how to remove background image
            }
        }
    }

    /**
     * Will find all the button id's and add them to an array list of imagebuttons
     * so that it may be easier to call, it will also set their onCLickListeners.
     */
    private void initializeButtons() {
        //Todo add more butttons
        carButtons = new ArrayList<>();
        themeButtons = new ArrayList<>();

        carButtons.add((ImageButton) findViewById(R.id.imageButton));
        carButtons.add((ImageButton) findViewById(R.id.imageButton3));
        carButtons.add((ImageButton) findViewById(R.id.imageButton2));

        //carButtons.add((ImageButton) findViewById(R.id.fourthCar));

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
            //try{
               // purchaser.closeShop(this);
                //Call to Advert popup activity.
                Intent myIntent = new Intent(ShopActivity.this, AdvActivity.class);
                ShopActivity.this.startActivityForResult(myIntent, 1);

                //purchaser = new Purchase(this, "Shop");

           // }catch(JSONException e){
            //    Toast.makeText(this, "problems occured", Toast.LENGTH_SHORT);
            //}
        }
        redrawScreen();
    }

    /**
     * When the user watches the advert presented in the AdvActivity, this method will be called and
     * add the apropraite anmount of ad reward points the users current amount of points
     *
     * @param requestCode, RESULT_OK if the user watched the advert presented else method would not be called
     * @param resultCode, 1 if the user came back from the AdvActivity, which is the only viable option
     * @param data, the intent returned by the AdvActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int newPoints = data.getIntExtra("Added points", 0);
                purchaser.addPoints(newPoints);
            }
        }
        redrawScreen();
    }


    /**
     * Overriden method for when the user leaves the shopactivity,
     * will save all changes made during the users shopping activity to
     * the json database.
     * Will also pause the music
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

    /**
     * Will resume the music playing previously, unless muted in the MenuActivity
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(muteFlag == 0) {
            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }
    }

    public void getPoints(View v)
    {
        Intent intent = new Intent(getApplicationContext(), AdvActivity.class);
        startActivity(intent);
    }
}

