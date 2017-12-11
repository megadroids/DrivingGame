package megadroid.drivinggame.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class to generate the shop logic to purchase the car and theme
 */

public class Purchase {

    private ScoreMonitor scoreMonitor;
    private String currentCar;
    private String currentTheme;
    private int points;
    private ArrayList<String> carlist;
    private ArrayList<String> themelist;

    private HashMap<String, Integer> carPrices;
    private HashMap<String, Integer> themePrices;

    /**
     * Constructor method to read the previous values from the JSON and also set price for the cars
     * @param context
     * @param screenType - it indicate if the json read is invoked from Shop screen
     * @throws JSONException - exception raised during Json read failure
     */
    public Purchase(Context context, String screenType) throws JSONException {

        carPrices = new HashMap<>();
        themePrices = new HashMap<>();

        carPrices.put("car_3", 200);
        carPrices.put("car_4", 300);
        carPrices.put("car_5", 400);
        carPrices.put("car_6", 750);


        scoreMonitor = new ScoreMonitor();
        scoreMonitor.readJSON(context, screenType);

        currentCar = scoreMonitor.getCurrentCar();
        currentCar = scoreMonitor.getCurrentCar();
        currentTheme = scoreMonitor.getCurrentTheme();
        carlist = scoreMonitor.getCarlist();
        themelist = scoreMonitor.getThemelist();
        points = scoreMonitor.getPoints();
    }

    /**
     * Method to verify if the car is already bought by the player
     * @param id
     * @return
     */
    public boolean isCarBought(String id) {
        Iterator<String> it = carlist.iterator();
        while(it.hasNext()) {
            if (it.next().equals(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * checks if the user has purchased the theme and is in the themelist
     *
     * @param id the string id of the theme to check
     * @return
     */
    public boolean isThemeBought(String id){
        Iterator<String> it = themelist.iterator();
        while(it.hasNext()){
            if(it.next().equals(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user has enough points to purchase the item selected
     *
     * @param itemToPurchase, the name of the item1 to check if affordable
     * @return true, if the user can afford the car chosen, else false
     */
    public boolean carAffordable(String itemToPurchase){
        if(carPrices.containsKey(itemToPurchase)) {
            int price = carPrices.get(itemToPurchase);
            if (points >= price) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if the user has enough points to unlock the theme they have selected
     *
     * @param itemToPurchase, the theme the user has clicked
     * @return true, if the user has enough points to purchase the theme selected, else false
     */
    public boolean themeAffordable(String itemToPurchase){
        if(themePrices.containsKey(itemToPurchase)) {
            int price = themePrices.get(itemToPurchase);
            if (points >= price) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * To add the car bought to the list of cars owned and to reduce the users points(currency)
     *
     * @param carToPurchase, car that's being bought
     */
    public void purchaseCar(String carToPurchase){
        int price = carPrices.get(carToPurchase);
        carlist.add(carToPurchase);
        points -= price;
    }

    /**
     * To add the theme bought to the list of themes owned and to reduce the users points(currency)
     *
     * @param themeToPurchase, theme that's being bought
     */
    public void purchaseTheme(String themeToPurchase){
        int price = themePrices.get(themeToPurchase);
        themelist.add(themeToPurchase);
        points -= price;
    }


    /**
     * a getter method for returning the amount of points the user currently has
     *
     * @return points, the amount of points the user currently has
     */
    public int getPoints(){
        return points;
    }

    /**
     * Adds a certain ammount of points to the current amount of points the user has.
     * called when the user watches an advert, though this could work if future implementation of purchasing points
     *
     * @param addedpoints, the points to add to current ammount of points
     */
    public void addPoints(int addedpoints){
        points += addedpoints;
    }

    /**
     * Saves all the data to the json databse after the user has pressed the back
     * A.K.A closing the shop
     *
     * @param context, the context in which has access to the system rescources
     * @throws JSONException, Writing to the database exception
     */
    public void closeShop(Context context) throws JSONException{

        scoreMonitor.writeJSON(context,-1,points,carlist,themelist,currentCar,currentTheme);
    }

    //Checkers and selectors. (A play on words, for getters and setters, I thought it was funny)
    /**
     * Checks if the car is the selected car for play
     *
     * @param carPressed, the car to check if it has been chosen by the user
     * @return true, if the car is in use as the playable car, else false
     */
    public boolean carSelected(String carPressed){
        return currentCar.equals(carPressed);
    }

    /**
     * Checks if the car is the selected car for play
     *
     * @param themePressed, the theme to check if it has been chosen by the user
     * @return true, if the car is in use as the playable car, else false
     */
    public boolean themeSelected(String themePressed){
        return currentTheme.equals(themePressed);
    }

    /**
     * Assigns the currentCar to the one selected by the user
     *
     * @param newCurrentCar, the theme pressed to be selected
     */
    public void selectCar(String newCurrentCar) {
        this.currentCar = newCurrentCar;
    }

    /**
     * Assigns the currentTheme to the one selected by the user
     *
     * @param newCurrentTheme, the theme pressed to be selected
     */
    public void selectTheme(String newCurrentTheme){
        this.currentTheme = newCurrentTheme;
    }

}
