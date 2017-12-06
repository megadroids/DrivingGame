package megadroid.drivinggame.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import megadroid.drivinggame.R;




public class exitButton extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupexit);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //the transition from MenuActivity to GameActivity
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
}
