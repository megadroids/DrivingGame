package megadroid.drivinggame.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import megadroid.drivinggame.model.SoundHelper;
import megadroid.drivinggame.model.JSONReader;
import megadroid.drivinggame.model.JSONWriter;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ScoreMonitor monitor;
    private SoundHelper msoundHelper;
    private int tagVal;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        monitor =new ScoreMonitor();

        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer2(this,R.raw.simple_game_music);
        msoundHelper.playMusic();

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
        muteSoundButton = (ImageButton) findViewById(R.id.Sound);
        exitButton = (ImageButton) findViewById(R.id.exit);

        //adding a click listener to buttons
        playButton.setOnClickListener(this);
        shopButton.setOnClickListener(this);
        muteSoundButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

        ImageView image = (ImageView) findViewById(R.id.Sound);
        image.setTag(Integer.valueOf(R.drawable.sound));

        //read scores from JSON file, initial json setup
        //ScoreMonitor monitor = new ScoreMonitor();
        String defaultCar = "def_car";
        String defaultImage = "backgroundcanvas";
        ArrayList<String> cars = new ArrayList<>();
        cars.add(defaultCar);
        ArrayList<String> themes = new ArrayList<>();
        themes.add(defaultImage );

        try {
            if(monitor.readJSON(this, "Menu").equals("")) {
                monitor.writeJSON(this, 0, 0, cars, themes, defaultCar, defaultImage );
            }
        } catch (JSONException e) {
            Log.e("JSONException",e.getMessage());
        }


        readJson();
    }

    // the onclick methods to handle clicking different buttons
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //the transition from MenuActivity to GameActivity
            case R.id.buttonPlay:
                //startActivity(new Intent(MenuActivity.this, GameActivity.class));
                Intent myIntent = new Intent(MenuActivity.this, GameActivity.class);
                myIntent.putExtra("muteFlag", tagVal ); //Optional parameters
                MenuActivity.this.startActivity(myIntent);

                break;

            //the transition from MenuActivity to ShopActivity
            case R.id.buttonShop:
                //startActivity(new Intent(MenuActivity.this, ShopActivity.class));
                Intent shopIntent = new Intent(MenuActivity.this, ShopActivity.class);
                shopIntent.putExtra("muteFlag", tagVal ); //Optional parameters
                MenuActivity.this.startActivity(shopIntent);

                break;

            case R.id.Sound:
                ImageView image = (ImageView) findViewById(R.id.Sound);
                if(image.getTag().equals((Integer.valueOf(R.drawable.mute_sound)))) {
                    image.setImageResource(R.drawable.sound);
                    image.setTag(Integer.valueOf(R.drawable.sound));
                    tagVal=0;
                    onResume();
            } else {
                    image.setImageResource(R.drawable.mute_sound);
                    image.setTag(Integer.valueOf(R.drawable.mute_sound));
                    tagVal=1;
                    onPause();
                }
                //tagVal = (Integer) image.getTag();
                break;

            case R.id.exit:
                //Intent startMain = new Intent(Intent.ACTION_MAIN);
                //startMain.addCategory(Intent.CATEGORY_HOME);
                //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(startMain);
                //break;

                startActivity(new Intent(MenuActivity.this,ExitActivity.class));

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readJson();
        if(tagVal == 0) {
            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }

    }

    private void readJson(){
        try {

            String values = monitor.readJSON(this.getApplicationContext(),"Menu");

            if(values.isEmpty()){
                Toast.makeText(this,"No Scores",Toast.LENGTH_LONG).show();
            }else
            {

                TextView txtHighscore = (TextView) findViewById(R.id.txtHighScore);
                txtHighscore.setText("Score : "+Integer.toString(monitor.getHighScore()));

            }

            //Toast.makeText(this,textToPrint,Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        msoundHelper.pauseMusic();

    }
}
