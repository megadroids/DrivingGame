package megadroid.drivinggame.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.MobileAds;

import megadroid.drivinggame.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //admob code
        MobileAds.initialize(this, "ca-app-pub-4000901278297592~9973472497");
    }

    //test code
    public void callAdvActivity(View view){
        Intent myIntent = new Intent(MainActivity.this, AdvActivity.class);

        MainActivity.this.startActivity(myIntent);
    }
}
