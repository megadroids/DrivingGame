package megadroid.drivinggame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Class used to generate the Items which should be collected by the player
 */

public class Items {
    private Bitmap bitmap;
    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;
    private int x;
    private int y;

    //creating a rect object
    private Rect detectCollision;

    /**
     * Constructor method used to generate the Items in a random position
     * @param context
     * @param screenX - the screen width
     * @param screenY - the screen height
     * @param bitmap the image source which will be drawn
     */
    public Items(Context context, int screenX, int screenY, Bitmap bitmap){
        this.bitmap = bitmap;
        maxY = screenY;
        minY= 0;
        maxX= screenX/4;
        minX= screenX/4;

        Random generator = new Random();
        speed = generator.nextInt(8)+5;
        y = generator.nextInt(minY + bitmap.getHeight()+200);
        x = minX;

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    /**
     * Method used to update the Item position
     * @param playerSpeed
     */
    public void update(int playerSpeed) {

        //increasing y coordinate so that item will move top to bottom
        y += playerSpeed;
        y += speed;
        //if the item reaches the bottom edge
        if (y > maxY - bitmap.getHeight()+200) {
            //adding the item again to the top edge
            Random generator = new Random();
            y = generator.nextInt(minY + bitmap.getHeight()+200);
            x = minX;
            speed+=1 ;
           }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    //adding a setter to x coordinate so that we can change it after collision
    public void setY(int y){
        this.y = y;
    }

    //getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    //method to get the bitmap image used for the items
    public Bitmap getBitmap() {
        return bitmap;
    }

    //method used to get the X position of the item
    public int getX() {
        return x;
    }

    //method used to get the Y position of the item
    public int getY() {
        return y;
    }

    //method used to get the speed of the item
    public int getSpeed() {
        return speed;
    }

    //method used to set the X position of the item
    public void setX(int x) {
        this.x = x;
    }
}
