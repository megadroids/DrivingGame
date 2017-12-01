package megadroid.drivinggame.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
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

    private SharedPreferences sharedPreferences;


    public Purchase(Context context, String screenType) throws JSONException {

        sharedPreferences = context.getSharedPreferences("Prices",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SecondCar", 0);
        editor.putInt("SecondTheme", 1000);
        editor.commit();

        scoreMonitor = new ScoreMonitor();
        scoreMonitor.readJSON(context, screenType);

        currentCar = scoreMonitor.getCurrentCar();
        currentTheme = scoreMonitor.getCurrentTheme();
        points = scoreMonitor.getPoints();
        carlist = scoreMonitor.getCarlist();
        themelist = scoreMonitor.getThemelist();
    }


    public boolean isCarBought(String id) {
        Iterator<String> it = themelist.iterator();
        while(it.hasNext()) {
            if (it.next().equals(id)){
                return true;
            }
        }
        return false;
    }

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
    public boolean itemAffordable(String itemToPurchase){
        int price = sharedPreferences.getInt(itemToPurchase, 0);
            if(points >= price){
                return true;
            }else{
                return false;
            }
    }

    public void purchaseCar(String carToPurchase){
            int price = sharedPreferences.getInt(carToPurchase, 0);
            carlist.add(carToPurchase);
            points -= price;
    }

    public void purchaseTheme(String themeToPurchase){
        int price = sharedPreferences.getInt(themeToPurchase, 0);
        points -= price;
        themelist.add(themeToPurchase);
    }





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
