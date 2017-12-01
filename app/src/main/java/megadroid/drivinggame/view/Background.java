package megadroid.drivinggame.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Megadroids .
 */

 class Background {

    private Bitmap image;
    private int x, y, dy;


    public Background(Bitmap res) {

            image = res;

        }

    public void update(int playerCounter){

        y += dy;
        if(y < -GameView.HEIGHT){
            y = 0;
            if(playerCounter%3 == 0) {
                dy -= 2;
            }
          if(dy > 60){
                dy = 60;
          }
        }

    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);
        if(y < 0){
            canvas.drawBitmap(image, x, y+GameView.HEIGHT, null);
        }
    }

    public void setVector(int dy) {
        this.dy = dy;

    }

}

    public void update(){
      //  y += dy;

        y -= dy;

//        if(y < -GameView.HEIGHT){
 //           y = 0;
 //       }

        if(y > GameView.HEIGHT){
            y = y-(int)GameView.HEIGHT;
        }


    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);

     //   if(y < 0){
      //      canvas.drawBitmap(image, x, y+GameView.HEIGHT, null);
      //  }

        if(y > 0){
            canvas.drawBitmap(image, x, y-GameView.HEIGHT, null);
              }
    }

    public void setVector(int dy) {

        this.dy = dy;
    }

}