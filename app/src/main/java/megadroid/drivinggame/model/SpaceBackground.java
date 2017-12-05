package megadroid.drivinggame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import megadroid.drivinggame.view.SpaceGameView;

/**
 * Created by joyag on 05/12/2017.
 */

public class SpaceBackground {

        private Bitmap image;
        private int x, y, dy;


        public SpaceBackground(Bitmap res) {

            image = res;
            y= (int) SpaceGameView.HEIGHT;
        }

        public void update(int playerCounter){

            //y += dy;
            y -= dy;
            //if(y < -GameView.HEIGHT){
            if(y > SpaceGameView.HEIGHT){
                // y = 0;
                y = y-(int)SpaceGameView.HEIGHT;
                if(playerCounter%3 == 0) {
                    dy += -2;
                }
                // if(dy == -60){
                //     dy = -60;
                // }



            }


        }

        public void draw(Canvas canvas){

            canvas.drawBitmap(image, x, y, null);
            //  if(y < 0){
            //      canvas.drawBitmap(image, x, y+SapceGameView.HEIGHT, null);
            //  }

            if(y > 0){
                canvas.drawBitmap(image, x, y-SpaceGameView.HEIGHT, null);
            }
        }

        public void setVector(int dy) {
            this.dy = dy;

        }


    }
