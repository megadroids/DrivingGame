package megadroid.drivinggame.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private int highScore = 0;
    private int points = 0;
    private ArrayList<String> carlist;
    private ArrayList<String> themelist;
    private ScoreMonitor monitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        carlist = new ArrayList<String>();
        themelist = new ArrayList<String>();
        monitor =new ScoreMonitor();

        //Crete image buttons
        ImageButton playButton;
        ImageButton shopButton;
        ImageButton muteSoundButton;
        ImageButton exitButton;

        //set the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //getting the buttons
        playButton = (ImageButton) findViewById(R.id.buttonPlay);
        shopButton = (ImageButton) findViewById(R.id.buttonShop);
        muteSoundButton = (ImageButton) findViewById(R.id.muteSound);
        exitButton = (ImageButton) findViewById(R.id.exit);

        //adding a click listener to buttons
        playButton.setOnClickListener(this);
        shopButton.setOnClickListener(this);
        muteSoundButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);


        //read scores from JSON file
        readJson();
    }

    // the onclick methods to handle clicking different buttons
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //the transition from MenuActivity to GameActivity
            case R.id.buttonPlay:
                startActivity(new Intent(MenuActivity.this, GameActivity.class));
                break;

            //the transition from MenuActivity to ShopActivity
            case R.id.buttonShop:
                startActivity(new Intent(MenuActivity.this, ShopActivity.class));
                break;

            case R.id.muteSound:
                break;

            case R.id.exit:
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readJson();
    }

    private void readJson(){
        //read scores from JSON file

        try {
            JSONArray values =  monitor.readJSON(this);

            if(values == null){
                Toast.makeText(this,"No Scores",Toast.LENGTH_LONG).show();
            }else
            {
                String textToPrint = "";
                for (int i = 0; i < values.length(); i++) {
                    try {
                        JSONObject message = (JSONObject) values.get(i);
                        highScore = message.getInt("highscore")  ;
                        points =  message.getInt("points")  ;

                        textToPrint += (message.get("highscore")) + "\n";
                        textToPrint += (message.get("points")) + "\n";

                        JSONArray carsArr = message.getJSONArray("cars");
                        for(i=0;i<carsArr.length();i++){
                            String car= (String) carsArr.get(i);

                            carlist.add(car);

                            textToPrint += car + "\n";
                        }

                        JSONArray themeArr = message.getJSONArray("themes");
                        for(i=0;i<themeArr.length();i++) {
                            String theme = (String) themeArr.get(i);

                            themelist.add(theme);

                            textToPrint += theme + "\n";
                        }

                    } catch (JSONException e) {
                        Log.e("JSONReadException",e.getMessage());
                    }

                }


                //Toast.makeText(this,textToPrint,Toast.LENGTH_LONG).show();

                TextView txtHighscore = (TextView) findViewById(R.id.txtHighScore);
                txtHighscore.setText("High Score : "+Integer.toString(highScore));

            }

        } catch (JSONException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
