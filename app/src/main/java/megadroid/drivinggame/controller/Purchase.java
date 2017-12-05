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
 * Created by megadroids on 11/23/2017.
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

    public Purchase(Context context, String screenType) throws JSONException {

        carPrices = new HashMap<>();
        themePrices = new HashMap<>();

        carPrices.put("car3", 500);
        carPrices.put("rocket", 700);
        themePrices.put("SecondTheme", 1000);

        scoreMonitor = new ScoreMonitor();
        scoreMonitor.readJSON(context, screenType);

        currentCar = scoreMonitor.getCurrentCar();
        if (currentCar == null){
            currentCar = "def_car";
        }
        currentTheme = scoreMonitor.getCurrentTheme();
        if(currentTheme == null){
            currentTheme = "def_theme";
        }

        points = scoreMonitor.getPoints();

        carlist = scoreMonitor.getCarlist();
        if (carlist == null){
            carlist = new ArrayList<>();
            carlist.add("def_car");
        }

        themelist = scoreMonitor.getThemelist();
        if(themelist == null){
            themelist = new ArrayList<>();
            themelist.add("def_theme");
        }
    }


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
     * @return true, if the user can afford the car chosen
     */
    public boolean carAffordable(String itemToPurchase){
        if(themePrices.containsKey(itemToPurchase)) {
            int price = carPrices.get(itemToPurchase);
            if (points >= price) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

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
     *
     *
     * @return the points that the
     */
    public int getPoints(){
        return points;
    }

    public void closeShop(Context context) throws JSONException{

        scoreMonitor.writeJSON(context,-1,points,carlist,themelist,currentCar,currentTheme);
    }



    //Checkers and setters
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

    public void selectCar(String newcurrentCar) {
        this.currentCar = newcurrentCar;
    }

    public void selectTheme(String newCurrentTheme){
        this.currentTheme = newCurrentTheme;
    }

}
