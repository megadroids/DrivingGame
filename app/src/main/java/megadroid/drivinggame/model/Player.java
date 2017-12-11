package megadroid.drivinggame.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import megadroid.drivinggame.view.GameView;

/**
 * Class to generate the player in the game
 */
public class Player {

    //Bitmap to get character from image
    private Bitmap bitmap;
    private Rect detectCollision;

    //coordinates
    private int x;
    private int y ;

    //motion speed of the character
    private int speed = 0;

    //boolean variable to track the ship is boosting or not
    private boolean boosting;

    //Gravity Value to add gravity effect on the ship
    private final int GRAVITY = -10;

    //Controlling Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY = 0;

    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private float Xpos;


    private int screenX;
    private int screenY;
    private int minX;
    private final int maxX;
    private boolean ontouch;

    /**
     * Constructor class used to generate the player and set the X and Y position
     * @param context
     * @param screenX - screen width
     * @param screenY - screen height
     * @param carID - the player bitmap resource ID to be assigned as player
     * @param currentTheme - the current selection of theme
     */
    public Player(Context context, int screenX, int screenY,int carID, String currentTheme) {
        x = screenX/2-30;
        y = screenY-330;//340;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), carID);

        //calculating maxY
        maxY = screenY -280;//340;//- bitmap.getHeight();

        //setting the boosting value to false initially
        boosting = false;

        if(currentTheme.equals("space_theme")) {
            maxX=screenX-bitmap.getWidth();
            minX= 0;

        }else {
            maxX=screenX/2+190;
            minX= screenX/2 -330;

        }

        Xpos=x;

        //initializing rect object
        detectCollision =  new Rect(x+10, y+10, bitmap.getWidth()-15, bitmap.getHeight()-15);

        //set touch to false
        ontouch= false;

    }

    /**
     * Method to update X and Y coordinate of character
     */
    public void update(){
        //updating x coordinate
        //if the ship is boosting
        if (!boosting) {
            //speeding up the ship
            speed += 2;
        } else {
            //slowing down if not boosting
            //speed -= 5;
            if(ontouch){
                //calculation on touch event
                if (Xpos < x) {
                    x = x - speed;
                }
                if (Xpos > x) {
                    x = x + speed;
                }

            }
            else {
                //calculation on sensor changed
                //moveleft
                if (Xpos > 0.5f) {

                    if (x > minX) {
                        x = x - speed;
                    } else {
                        x = minX;
                    }
                }
                //moveRight
                if (Xpos < -0.5f) {

                    if (x < maxX) {
                        x = x + speed;
                    } else {
                        x = maxX;
                    }
                }
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

        //but controlling it also so that it won't go off the screen
        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x+10;
        detectCollision.top = y+10;
        detectCollision.right = x + bitmap.getWidth()-15;
        detectCollision.bottom = y + bitmap.getHeight()-15;

    }

    //setting boosting true
    public void setBoosting(float cellX,boolean touchflag) {
        boosting = true;
        Xpos = cellX;
        ontouch = touchflag;
    }

    //setting boosting false
    public void stopBoosting() {
        boosting = false;

    }

    //getter method for the bitmp image
    public Bitmap getBitmap() {
        return bitmap;
    }

    //method to get the X position of the player
    public int getX() {
        return x;
    }

    //method to get the Y position of the player
    public int getY() {
        return y;
    }

    //method to get the speed of the player
    public int getSpeed() {
        return speed;
    }

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    /**
     * Method to calculate the X position of the player based on the tilt of the device
     */
    public void updatetilt() {

        //Calculating velocity & acceleration for smooth side to side movement
        float frameTime = 1.666f;
        GameView.xVel = (GameView.xAccel * frameTime);

        float xS = (GameView.xVel / 2) * frameTime;
        x -= xS;

        if (x > maxX) {
            x = maxX;
        } else if (x < minX) {
            x = minX;
        }

    }
}