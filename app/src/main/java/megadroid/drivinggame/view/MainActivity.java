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

public class MainActivity extends AppCompatActivity {
    private SoundHelper msoundHelper;
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
        // getSupportActionBar().hide();

        //Crete image button
        msoundHelper = new SoundHelper(this);
        msoundHelper.prepareMusicPlayer3(this,R.raw.start_page);
        msoundHelper.playMusic();
        ImageButton startButton;

        //getting the button

        //startButton = (ImageButton) findViewById(R.id.buttonStart);

        //adding a click listener to Start button

        //startButton.setOnClickListener(this);


    }

    // onClick method
    /** @Override
    public void onClick(View v) {
    switch (v.getId()) {
    //the transition from MenuActivity to GameActivity
    case R.id.buttonStart:
    startActivity(new Intent(MainActivity.this, MenuActivity.class));
    break;
    }
    }
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        }
        return true;
    }

    protected void onPause() {
        super.onPause();

        msoundHelper.pauseMusic();

    }
}
