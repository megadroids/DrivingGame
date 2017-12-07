package megadroid.drivinggame.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


import megadroid.drivinggame.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();

        //Crete image button

        ImageButton startButton;

        //getting the button

        startButton = (ImageButton) findViewById(R.id.buttonStart);

        //adding a click listener to Start button

        startButton.setOnClickListener(this);

    }

    // onClick method
    @Override
    public void onClick(View v) {
         switch (v.getId()) {
             //the transition from MenuActivity to GameActivity
             case R.id.buttonStart:
                 startActivity(new Intent(MainActivity.this, MenuActivity.class));
                 break;
         }
    }
}


