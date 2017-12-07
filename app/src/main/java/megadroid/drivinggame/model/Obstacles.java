package megadroid.drivinggame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

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


    public Obstacles(Context context, int screenX, int screenY, Bitmap bitmap , int Xstart, int Xend) {
        this.bitmap = bitmap;
       // bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
       // maxX = screenX;
        maxY = screenY;
        maxX=Xend;
        minX= Xstart ;//Xstart -(80*range);

        //minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(8) + 15;
        x = generator.nextInt(maxX - bitmap.getWidth());//screenX;
        y = 0 - bitmap.getHeight();//maxY;//generator.nextInt(maxY) - bitmap.getHeight();

        //initializing rect object
        detectCollision = new Rect(x+10, y+10, bitmap.getWidth()-10, bitmap.getHeight()-20);


    }

    public void update(int playerSpeed) {
       // y += playerSpeed;
        y += speed;
        if (y > maxY + bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(15) + 20;
            x = generator.nextInt(maxX - bitmap.getWidth());//maxX;
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

        detectCollision.right = x + bitmap.getWidth()-5;
        detectCollision.bottom = y + bitmap.getHeight()-20;

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

    public void setX(int newX){ this.x = newX;}

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
