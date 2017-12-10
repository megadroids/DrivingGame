package megadroid.drivinggame.model;

/**
 * Created by megadroids on 11/23/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import megadroid.drivinggame.view.GameView;

/**
 * Created by megadroids .
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
    //constructor
    public Player(Context context, int screenX, int screenY,int carID) {
        x = screenX/2-30;
        y = screenY-340;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), carID);

        //calculating maxY
        maxY = screenY -340 ;//- bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
       // minY = -200;//0;

        //setting the boosting value to false initially
        boosting = false;

        maxX=screenX/2+190;
        minX= screenX/2 -330;
        Xpos=x;



        //initializing rect object
        detectCollision =  new Rect(x+10, y+10, bitmap.getWidth()-15, bitmap.getHeight()-15);

        //set touch to false
        ontouch= false;

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

        //moving the ship down
        /*
        y -= speed + GRAVITY;



        //but controlling it also so that it won't go off the screen
        if (y < minY) {
            y = maxY;
        }
        if (y > maxY) {
            y = maxY;
        }

        */

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

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }


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