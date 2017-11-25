package megadroid.drivinggame.view;

/**
 * Created by Megadroids on 11/24/2017.
 */
   import android.graphics.Bitmap;
   import android.graphics.Canvas;

/**
 * Created by joyag on 24/11/2017.
 */

public class Background {
    private Bitmap image;
    private int x, y, dy;

    public Background(Bitmap res){

        image = res;
    }

    public void update(){

        y+=dy;
        if(y<-GameView.HEIGHT) {
            y = 0;
        }

    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);
        if(y<0){
            canvas.drawBitmap(image, x, y+GameView.HEIGHT, null);
        }
    }

    public void setVector(int dy){
        this.dy = dy;


    }
}