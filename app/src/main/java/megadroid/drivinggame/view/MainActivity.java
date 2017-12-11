package megadroid.drivinggame.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import megadroid.drivinggame.R;
import megadroid.drivinggame.model.SoundHelper;

/**
 * Class used to generate the Game Start screen
 */
public class MainActivity extends AppCompatActivity {
    private SoundHelper msoundHelper;

    /**
     * Method invoked on the creation of activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        TextView myText = (TextView) findViewById(R.id.touch);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/gomarice_no_continue.ttf");
        myText.setTypeface(custom_font);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(800); //You can manage the time of the blink with this parameter
        anim.setStartOffset(45);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        myText.startAnimation(anim);

        //Crete image button
        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer3(this,R.raw.start_page);
        msoundHelper.playMusic();
        ImageButton startButton;

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
     * Method invoked when user touches the screen to begin game
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        }
        return true;
    }

    /**
     * Method invoked when activity is paused
     */
    protected void onPause() {
        super.onPause();

        //stop music when going to Menu activity
        if(msoundHelper != null) {
            msoundHelper.stopMusic();
            msoundHelper = null;
        }

    }
}
