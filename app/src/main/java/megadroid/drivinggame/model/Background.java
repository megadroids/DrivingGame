package megadroid.drivinggame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import megadroid.drivinggame.view.GameView;

/**
 * Created by Megadroids .
 */

public class Background {

    private Bitmap image;
    private int x, y, dy;


    public Background(Bitmap res) {

            image = res;
        y= (int) GameView.HEIGHT;
        }

    public void update(int playerCounter){

      //  y += dy;
        y -= dy;
        //if(y < -GameView.HEIGHT){
        if(y > GameView.HEIGHT){
           // y = 0;
            y = y-(int)GameView.HEIGHT;
         /*   if(playerCounter%3 == 0) {
                dy = 2;
            }
          if(dy > 60){
                dy = 60;
          }
*/

        }


    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);
      //  if(y < 0){
      //      canvas.drawBitmap(image, x, y+GameView.HEIGHT, null);
      //  }

        if(y > 0){
            canvas.drawBitmap(image, x, y-GameView.HEIGHT, null);
        }
    }

    public void setVector(int dy) {
        this.dy = dy;

    }


}