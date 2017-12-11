package megadroid.drivinggame.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import megadroid.drivinggame.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Class to load video ads and add reward points to user account
 */

public class AdvActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    private static final int POINTS_REWARD = 25;
    private ProgressDialog dialog;
    private boolean showad;

    /**
     * Method invoked on creation of activity and initialises and loads the admob object
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);

        TextView myText = (TextView) findViewById(R.id.title_text_view);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/gomarice_no_continue.ttf");
        myText.setTypeface(custom_font);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        showad = false;
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

        //hide the bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //admob code with real ad unit ID (code to be used)
    /*
    private void loadRewardedVideoAd() {
         mRewardedVideoAd.loadAd("ca-app-pub-1558090702648041/5920447341",
              new AdRequest.Builder().build());
    }
    */

    /**
     * test code to load ads with test ad unit ID     *
     */
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

    /**
     * method to indicate that the ad was viewed completely and should be rewarded     *
     * @param reward  - the points earned
     */
    @Override
    public void onRewarded(RewardItem reward) {
        // Rewards the user
        Toast.makeText(this, "You have earned " + POINTS_REWARD +" points!!",Toast.LENGTH_SHORT ).show();
        Intent intent = new Intent();
        intent.putExtra("Added points", POINTS_REWARD);
        setResult(RESULT_OK, intent);
    }

    /**
     * Method invoked when videoAd completes
     */
    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication",
        //Toast.LENGTH_SHORT).show();
    }

    /**
     * method invoked on closure of ad window     *
      */
    @Override
    public void onRewardedVideoAdClosed() {
        finish();
    }

    /**
     * Method invoked when there is an problem in loading the ad
     * @param errorCode
     */
    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "Ad Failed To Load. Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Method invoked when the Video ad is loaded completely
     */
    @Override
    public void onRewardedVideoAdLoaded() {
        if(showad) {
            showad = false;
            dialog.dismiss();
            mRewardedVideoAd.show();

        }
    }

    /**
     * Method invoked when video ad is opened
     */
    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method invoked when video ad is started
     */
    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method invoked when activity is resumed from pause status
     */
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    /**
     * Method invoked when the activity is paused due to another activity
     */
    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    /**
     * Method invoked when activity is destroyed
     */
    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    /**
     * On click event for the show ad button in the ad popup
     * @param view
     */
    public void showAdv(View view) {

        showad = true;
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }else {
            dialog.show(this, "Loading Ads", "The video is loading. Please wait...", true);

        }
    }

    /**
     * On click event for the close button on the show ad popup
     * @param view
     */
    public void close(View view) {
        this.finish();
    }

    /**
     * Method invoked on back button press
     */
    @Override
    public void onBackPressed() {
        //do nothing so that the back pressed is disabled
    }

}
