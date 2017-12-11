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

/**
 * Class used to generate the Play Menu screen
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ScoreMonitor monitor;
    private SoundHelper msoundHelper;
    private int tagVal;

    /**
     * Method invoked on creation of activity and used to initialise the UI elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        tagVal = intent.getIntExtra("muteFlag",0);

        monitor =new ScoreMonitor();
        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer2(this,R.raw.simple_game_music);
        msoundHelper.playMusic();

        //Create image buttons
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

        readJson();

        //hide the bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * the onclick method to handle clicking of the buttons in the screen
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //the transition from MenuActivity to GameActivity
            case R.id.buttonPlay:
                Intent myIntent = new Intent(MenuActivity.this, GameActivity.class);
                myIntent.putExtra("muteFlag", tagVal ); //Optional parameters
                MenuActivity.this.startActivity(myIntent);

                break;

            //the transition from MenuActivity to ShopActivity
            case R.id.buttonShop:
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
                    if(tagVal == 0) {
                        msoundHelper.playMusic();
                    }else
                    {
                        msoundHelper.pauseMusic();
                    }
                 } else {
                    image.setImageResource(R.drawable.mute_sound);
                    image.setTag(Integer.valueOf(R.drawable.mute_sound));
                    tagVal=1;
                    if(tagVal == 0) {
                        msoundHelper.playMusic();
                    }else
                    {
                        msoundHelper.pauseMusic();
                    }
                }
                break;

            case R.id.exit:
                 startActivity(new Intent(MenuActivity.this,ExitActivity.class));

            default:
                break;
        }
    }

    /**
     * Method invoked when activity is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        readJson();

        if(msoundHelper == null){
            msoundHelper = new SoundHelper(this);
            msoundHelper.prepareMusicPlayer2(this,R.raw.simple_game_music);
        }
        ImageView image = (ImageView) findViewById(R.id.Sound);
        if(tagVal == 0) {
            msoundHelper.playMusic();
            image.setImageResource(R.drawable.sound);
            image.setTag(Integer.valueOf(R.drawable.sound));
        }else
        {
            image.setImageResource(R.drawable.mute_sound);
            image.setTag(Integer.valueOf(R.drawable.mute_sound));
            msoundHelper.pauseMusic();
        }

        //hide the bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * Method used to read the Json file and load with defaults if file is empty
     */
    private void readJson(){
        try {

            String values = monitor.readJSON(this.getApplicationContext(),"Menu");

            if(values.isEmpty()){
                //Initial json setup, load default values
                ScoreMonitor monitor = new ScoreMonitor();
                ArrayList<String> cars = new ArrayList<>();
                cars.add("def_car");
                cars.add("car_2");
                ArrayList<String> themes = new ArrayList<>();
                themes.add("backgroundcanvas");
                themes.add("space_theme");

                try {
                        monitor.writeJSON(this, 0, 0, cars, themes, "def_car", "backgroundcanvas");
                } catch (JSONException e) {
                    Log.e("JSONException",e.getMessage());
                }

            }else
            {
                TextView txtHighscore = (TextView) findViewById(R.id.txtHighScore);
                txtHighscore.setText("Score : "+Integer.toString(monitor.getHighScore()));
            }

        } catch (JSONException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method used invoked when activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();

        //stop music when going to Game activity
        msoundHelper.pauseMusic();
        msoundHelper.pauseMusic();
        msoundHelper.stopMusic();
        msoundHelper = null;
    }

    /**
     * Method invoked on back button press
     */
    @Override
    public void onBackPressed() {
        //do nothing so that the back pressed is disabled
    }


}
