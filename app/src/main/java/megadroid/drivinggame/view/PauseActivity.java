package megadroid.drivinggame.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import megadroid.drivinggame.R;

public class PauseActivity extends AppCompatActivity implements Button.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        //Create image buttons
        ImageButton menuButton;
        ImageButton resumeButton;

        //set the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //getting the buttons
        menuButton = (ImageButton) findViewById(R.id.menu);
        resumeButton = (ImageButton) findViewById(R.id.resume);

        //adding a click listener to buttons
        menuButton.setOnClickListener(this);
        resumeButton.setOnClickListener(this);

    }

    // the onclick methods to handle clicking different buttons
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //the transition from MenuActivity to GameActivity
            case R.id.menu:
                startActivity(new Intent(PauseActivity.this, MenuActivity.class));
                break;

            //the transition from MenuActivity to ShopActivity
            case R.id.resume:
                //startActivity(new Intent(PauseActivity.this, GameActivity.class));
                this.finish();
                break;


            default:
                break;
        }
    }
}
