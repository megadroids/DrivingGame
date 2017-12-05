package megadroid.drivinggame.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONException;

public class AdvActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    Button showAdvButton;
    private static final int POINTS_REWARD = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);

        showAdvButton = ((Button) findViewById(R.id.button_adv));
        showAdvButton.setEnabled(false);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    //admob code with real ad unit ID (code to be used)
    /*
    private void loadRewardedVideoAd() {
         mRewardedVideoAd.loadAd("ca-app-pub-1558090702648041/5920447341",
              new AdRequest.Builder().build());
    }
    */
    //test code with test ad unit ID

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    //test code with real ad unit ID and bluestacks test device ID
    /*
    private void loadRewardedVideoAd() {
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("A00000820EAC16")
                .build();
        mRewardedVideoAd.loadAd("ca-app-pub-1558090702648041/5920447341",
                request);
    }
    */

    @Override
    public void onRewarded(RewardItem reward) {
        //Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                //reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Rewards the user
        ScoreMonitor monitor = new ScoreMonitor();
        /*
        try {
            monitor.readJSON(this, "Shop");
            int totalPoints = monitor.getPoints() + POINTS_REWARD;
            monitor.writeJSON(this, monitor.getHighScore(), totalPoints, monitor.getCarlist(),
                    monitor.getThemelist(), monitor.getCurrentCar(), monitor.getCurrentTheme());
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }*/

        Intent intent = new Intent();
        intent.putExtra("Added points", POINTS_REWARD);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        showAdvButton.setEnabled(false);
        //loadRewardedVideoAd();
        //Intent myIntent = new Intent(AdvActivity.this, ShopActivity.class);
        //AdvActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        //Intent myIntent = new Intent(AdvActivity.this, ShopActivity.class);
        //AdvActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        showAdvButton.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    public void showAdv(View view){
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

}
