package megadroid.drivinggame.model;

/**
 * Created by megadroids on 11/23/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import megadroid.drivinggame.R;

/**
 * Created by megadroids .
 */
public class Player {

    //Bitmap to get character from image
    private Bitmap bitmap;

    private Rect detectCollision;

    //coordinates
    private int x;
    private int y;

    //motion speed of the character
    private int speed = 0;

    //boolean variable to track the ship is boosting or not
    private boolean boosting;

    //Gravity Value to add gravity effect on the ship
    private final int GRAVITY = -10;

    //Controlling Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY;

    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private int Xpos;


    private int screenX;
    private int screenY;
    private int minX;
    private final int maxX;

    //constructor
    public Player(Context context, int screenX, int screenY) {
        x = screenX/2-30;
        y = screenY;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.car);

        //calculating maxY
        maxY = screenY ;//- bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = -200;//0;

        //setting the boosting value to false initially
        boosting = false;

        maxX=screenX/2 +300;
        minX= screenX/2 - 300;
        Xpos=x;

        //initializing rect object
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    //Method to update coordinate of character
    public void update(){
        //updating x coordinate
        //if the ship is boosting
        if (!boosting) {
            //speeding up the ship
            speed += 2;
        } else {
            //slowing down if not boosting
            //speed -= 5;
            if(Xpos < x){
                x= x -speed;
            }
            if(Xpos > x){
                x= x +speed;
            }

        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //moving the ship down
        y -= speed + GRAVITY;

        //but controlling it also so that it won't go off the screen
        if (y < minY) {
            y = maxY;
        }
        if (y > maxY) {
            y = maxY;
        }

        //but controlling it also so that it won't go off the screen
        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }
    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    //setting boosting true
    public void setBoosting(int cellX) {
        boosting = true;
        Xpos = cellX;
    }

    //setting boosting false
    public void stopBoosting() {
        boosting = false;

    }



    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}