package megadroid.drivinggame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import megadroid.drivinggame.R;

/**
 * Created by megadroids on 11/23/2017.
 */

public class Items {
    private Bitmap bitmap;

    private int speed =1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    private int x;
    private int y;

    //creating a rect object
    private Rect detectCollision;



    public Items(Context context, int screenX, int screenY){

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_gold);
        maxX = screenX - 200;
        maxY = screenY;

        minX = screenX - 400;
        minY=0;

        Random generator = new Random();
        speed = generator.nextInt(5) + 5;
        y = 0;
        x = generator.nextInt(maxX) - bitmap.getWidth();



        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());



    }
    public void update(int playerSpeed) {
        //decreasing x coordinate so that item will move top to bottom
        y += playerSpeed;
        y += speed;
        //if the item reaches the top edge
        if (y > maxY - bitmap.getHeight()) {
            //adding the item again to the right edge
            Random generator = new Random();
            speed = generator.nextInt(3) + 5;
            x = generator.nextInt(maxX) - bitmap.getWidth();
            y = minY;
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

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }


    //getters
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
