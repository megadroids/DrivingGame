package megadroid.drivinggame.model;

import java.util.Random;

/**
 * Class used to generate the stars in the game
 */

public class Star {
    private int x;
    private int y;
    private int speed;

    private int maxY;
    private int maxX;
    private int minX;
    private int minY;
    private String currentTheme;
    private Random generator;
    private float starWidth;

    /**
     * Constructor method to set the X and Y position of the star object
     * @param screenX - screen width
     * @param screenY - screen height
     * @param currentTheme - the current theme selected by the user
     */
    public Star(int screenX, int screenY,String currentTheme){
        maxX = screenX;
        maxY = screenY;

        minX =0;
        minY=0;
        generator = new Random();
        speed = generator.nextInt(10);
        this.currentTheme = currentTheme;
            x = generator.nextInt(maxX);
            y = generator.nextInt(maxY+200);

    }

    /**
     * Method used to set the X and Y position of the star object
     * @param playerSpeed
     */
    public void update(int playerSpeed){
        if(currentTheme.equals("space_theme")){
            y += playerSpeed;
            y += speed;
            if (y > maxY + 200) {
                x = generator.nextInt(maxX);;
                y = 0;
                speed = generator.nextInt(15);

            }
        }
        else {
            x -= playerSpeed;
            x -= speed;

            if (x < 0) {
                x = maxX;
                Random generator = new Random();
                y = generator.nextInt(maxY+200);
                speed = generator.nextInt(15);

            }
        }
    }

    //method to get the random star width
    public float getStarWidth(){
        float minX  = 1.0f;
        float maxX = starWidth;//6.0f;

        Random rand = new Random();
        float finalX = rand.nextFloat()*(maxX-minX)+minX;
        return finalX;
    }

    //get X position of the star
    public int getX() {
        return x;
    }

    //get Y position of the star
    public int getY() {
        return y;
    }

    //set the widthh of the star object
    public void setStarWidth(float starWidth) {
        this.starWidth = starWidth;
    }
}

