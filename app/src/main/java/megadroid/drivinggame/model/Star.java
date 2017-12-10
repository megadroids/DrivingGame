package megadroid.drivinggame.model;

import java.util.Random;

/**
 * Created by megadroids
 */

public class Star {
    private int x;
    private int y;
    private int speed;

    private int maxY;
    private int maxX;
    private int minX;
    private int minY;

    private float starWidth;


    public Star(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;

        minX =0;
        minY=0;
        Random generator = new Random();
        speed = generator.nextInt(10);

        x= generator.nextInt(maxX);
        y=generator.nextInt(maxY);
    }

    public void update(int playerSpeed){
        x-= playerSpeed;
        x -=speed;

        if(x<0){
            x=maxX;
            Random generator = new Random();
            y= generator.nextInt(maxY);
            speed=generator.nextInt(15);

        }
    }

    public float getStarWidth(){
        float minX  = 1.0f;
        float maxX = starWidth;//6.0f;

        Random rand = new Random();
        float finalX = rand.nextFloat()*(maxX-minX)+minX;
        return finalX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setStarWidth(float starWidth) {
        this.starWidth = starWidth;
    }
}

