package megadroid.drivinggame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import megadroid.drivinggame.R;

/**
 * Created by megadroids.
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


    public Obstacles(Context context, int screenX, int screenY, Bitmap bitmap ) {
        this.bitmap = bitmap;
       // bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
       // maxX = screenX;
        maxY = screenY;
        maxX=screenX/2+200;
        minX= screenX/2 -180;

        //minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 15;
        x = generator.nextInt(maxX) - bitmap.getWidth();//screenX;
        y = 0;//maxY;//generator.nextInt(maxY) - bitmap.getHeight();

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());


    }

    public void update(int playerSpeed) {
        y += playerSpeed;
        y += speed;
        if (y > maxY + bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(6) + 15;
            x = generator.nextInt(maxX) - bitmap.getWidth();//maxX;
            y = 0;//maxY;//generator.nextInt(maxY) - bitmap.getHeight();
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
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

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


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
