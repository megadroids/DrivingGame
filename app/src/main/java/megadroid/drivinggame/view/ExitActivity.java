package megadroid.drivinggame.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import megadroid.drivinggame.R;

/**
 * Class used for Exit poup logic
 */
public class ExitActivity extends Activity implements View.OnClickListener {

    /**
     * Method invoked on create of activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

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
     * Method used to handle the onclick event of the exit button and close button
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cross:
                this.finish();
                break;

            case R.id.exit_app:
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                this.finish();
                break;

            default:
                break;
        }

    }

    /**
     * Method invoked on back button press
     */
    @Override
    public void onBackPressed() {
        //do nothing so that the back pressed is disabled
    }


}
