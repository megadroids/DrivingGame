package megadroid.drivinggame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import megadroid.drivinggame.view.GameView;

/**
 * Class includes logic to generate the scrolling background of the game
 */

public class Background {

    private Bitmap image;
    private int x, y, dy;

    /**
     * constructor used to set the background image     *
      * @param res
     */
    public Background(Bitmap res) {
        image = res;
        y= (int) GameView.HEIGHT;
    }

    /**
     * Method used to update the background position and create scrolling effect
     * @param playerCounter
     */
    public void update(int playerCounter){

        y -= dy;
        if(y > GameView.HEIGHT){
            y = y-(int)GameView.HEIGHT;
            if(playerCounter%3 == 0) {
                dy += -2;
            }

            //setting max limit for the background scrolling
            if(dy == -60){
                dy = -60;
           }

        }

    }

    /**
     * Method used to draw the background image on the canvas in the location updated
     * @param canvas
     */
    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);

        if(y > 0){
            canvas.drawBitmap(image, x, y-GameView.HEIGHT, null);
        }
    }

    //Method to set the rate of background position update
    public void setVector(int dy) {
        this.dy = dy;
    }

    //Method to get the value of vector used for updating the background
    public int getVector() {
        return this.dy ;
    }

}