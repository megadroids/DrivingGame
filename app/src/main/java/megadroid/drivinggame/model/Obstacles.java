package megadroid.drivinggame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Class used to generate the obstacles blocking the player
 */

public class Obstacles {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    //creating a rect object for a friendly ship
    private Rect detectCollision;

    /**
     * Constructor method used to generate the obstacle object and set its position on teh canvas
     * @param context
     * @param screenX - screen width
     * @param screenY - screen height
     * @param bitmap - image used fir the obstacle
     * @param Xstart - start position of the obstacle
     * @param Xend - end position of the obstacle
     */
    public Obstacles(Context context, int screenX, int screenY, Bitmap bitmap , int Xstart, int Xend) {
        this.bitmap = bitmap;
        maxY = screenY;
        maxX=Xend;
        minX= Xstart ;

        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(8) + 5;
        x = generator.nextInt(maxX) - bitmap.getWidth();
        y = 0 - bitmap.getHeight();

        //initializing rect object
        detectCollision = new Rect(x+10, y+10, bitmap.getWidth()-10, bitmap.getHeight()-15);


    }

    /**
     * Method to update the position of the obstacle
     * @param playerSpeed
     */
    public void update(int playerSpeed) {
        y += playerSpeed;
        y += speed;
        if (y > maxY + bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(15) + 5;
            x = generator.nextInt(maxX) - bitmap.getWidth();//maxX;
            y = 0 - bitmap.getHeight();//maxY;//generator.nextInt(maxY) - bitmap.getHeight();
        }

        //but controlling it also so that it won't go off the screen
        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x+10;
        detectCollision.top = y+10;
        detectCollision.right = x + bitmap.getWidth()-10;
        detectCollision.bottom = y + bitmap.getHeight()-15;

    }

    //getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    //getters for the bitamp used as obstacle
    public Bitmap getBitmap() {
        return bitmap;
    }

    //to get the X position of the obsatcle
    public int getX() {
        return x;
    }

    //to get the Y position of the obstacle
    public int getY() {
        return y;
    }

    //to set the X position of the obstacle
    public void setX(int newX){ this.x = newX;}

    //to set the bitmap image for the obstacle
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
