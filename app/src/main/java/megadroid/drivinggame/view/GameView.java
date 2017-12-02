package megadroid.drivinggame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import megadroid.drivinggame.R;
import megadroid.drivinggame.model.Items;
import megadroid.drivinggame.model.Boom;
import megadroid.drivinggame.model.Obstacles;
import megadroid.drivinggame.model.Player;
import megadroid.drivinggame.model.Star;
import megadroid.drivinggame.model.SoundHelper;

/**
 * Created by megadroids.
 */

public class GameView extends SurfaceView implements Runnable,SensorEventListener {

    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor gyroscopeSensor;
    private float xAcceleration,yAcceleration,zAcceleration;


    //properties of the background image and instantiation of the background class
    private GameActivity ga = new GameActivity();
    private Items[] item;
    private Items[] item1;
    //Adding 3 items you
    private int itemCount = 2;
    private ArrayList<Star> stars = new ArrayList<Star>();

    //Controls speed of the background scroll
    // public static final int MOVESPEED = -10;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    volatile int playingCounter=0;

    //the game thread
    private Thread gameThread = null;

    //adding the player to this class
    private Player player;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //created a reference of the class Friend
    private Obstacles obstacles;
    private Obstacles obstacles2;

    //an indicator if the game is Over
    private boolean isGameOver ;

    //defining a boom object to display blast
    private Boom boom;

    //properties of the background image and instantiation of the background class
    public static float WIDTH ;//640;
    public static float HEIGHT ;//1440;
    private Background bg;

    private int screenX;
    private int screenY;

    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);

        //declaring Sensor Manager and sensor type
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor =  manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


// Register the listener
       manager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        //initializing player object
        //this time also passing screen size to player constructor
        player = new Player(context, screenX, screenY);

        int starNums = 800;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);

        }


        Bitmap bitmapCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_gold);
        Bitmap bitmapCrystal = BitmapFactory.decodeResource(context.getResources(), R.drawable.crystal);
        item = new Items[itemCount];
        for (int j = 0; j < itemCount; j++) {

            item[j] = new Items(this.getContext(), screenX*2 -450 , screenY, bitmapCoin);
        }

        item1 = new Items[itemCount];
        for (int k = 0; k < itemCount; k++) {

            item1[k] = new Items(this.getContext(), screenX *3 -150, screenY, bitmapCoin);
        }

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //initializing boom object
        boom = new Boom(context);

        //initializing the Friend class object
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);
        Bitmap bitmapcar = BitmapFactory.decodeResource(this.getResources(), R.drawable.racecar);

  /*      if((playCounter%2)==0){
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);
        }else
        {
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.racecar);
        }
*/
        obstacles = new Obstacles(this.getContext(), screenX, screenY,bitmap,screenX/2,3);
        obstacles2 = new Obstacles(this.getContext(), screenX, screenY,bitmapcar,screenX/2+200,1);

        isGameOver = false;

        this.screenX = screenX;
        this.screenY = screenY;



    }
    public void run() {
        while (playing) {
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }



    private void update() {


        //updating player position
        player.update();


        bg.update(playingCounter);

        // update the stars
        for(Star s : stars) {
            s.update(player.getSpeed());

        }

        for (int i = 0; i < itemCount; i++) {

            item[i].update(player.getSpeed() );

            //if collision occurrs with player
            if (Rect.intersects(player.getDetectCollision(), item[i].getDetectCollision())) {
                //moving item outside the topedge
                item[i].setY(-200);

            }

        }

        for (int j = 0; j < itemCount; j++) {

            item1[j].update(player.getSpeed());

            //if collision occurrs with player
            if (Rect.intersects(player.getDetectCollision(), item1[j].getDetectCollision())) {
                //moving item outside the topedge
                item1[j].setY(-200);
            }
        }

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);


        //updating the friend ships coordinates
        obstacles.update(player.getSpeed());
        obstacles2.update(player.getSpeed()+10);

        //checking for a collision between player and a friend
        if (playingCounter > 100) {

            if (Rect.intersects(player.getDetectCollision(), obstacles.getDetectCollision())) {

                //displaying the boom at the collision
                boom.setX(obstacles.getX());
                boom.setY(obstacles.getY());
                //setting playing false to stop the game
                         playing = false;
                //setting the isGameOver true as the game is over
                          isGameOver = true;
            }
        }

        if (playingCounter > 50) {

            if (Rect.intersects(player.getDetectCollision(), obstacles2.getDetectCollision())) {

                //displaying the boom at the collision
                boom.setX(obstacles2.getX());
                boom.setY(obstacles2.getY());
                //setting playing false to stop the game
                         playing = false;
                //setting the isGameOver true as the game is over
                           isGameOver = true;
            }
        }

    }



    private void draw() {

        playingCounter++;

        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {

            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);


            //Scaling the background for different sizes of screens
            float scaleFactorX = (float) screenX / (WIDTH * 1.f);
            float scaleFactorY = (float) screenY / (HEIGHT * 1.f);

            if (canvas != null) {
                //Saving the state of the canvas before scaling
                final int savedState = canvas.save();
                canvas.scale(scaleFactorX, scaleFactorY);
                bg.draw(canvas);
                canvas.restoreToCount(savedState);

            }

            if (playingCounter > 100) {
                //drawing the items
                for (int i = 0; i < itemCount; i++) {
                    canvas.drawBitmap(
                            item[i].getBitmap(),
                            item[i].getX(),
                            item[i].getY(),
                            paint
                    );
                }
            }

            if (playingCounter > 200) {
                //drawing the items
                for (int i = 0; i < itemCount; i++) {
                    canvas.drawBitmap(
                            item1[i].getBitmap(),
                            item1[i].getX(),
                            item1[i].getY(),
                            paint
                    );
                }
            }


            //Draw the stars and set colour to white
            paint.setColor(Color.WHITE);
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }


            //Drawing the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing obstacles image
            if (playingCounter > 100) {

                canvas.drawBitmap(

                        obstacles.getBitmap(),
                        obstacles.getX(),
                        obstacles.getY(),
                        paint
                );
            }
            if (playingCounter > 50) {

                canvas.drawBitmap(

                        obstacles2.getBitmap(),
                        obstacles2.getX(),
                        obstacles2.getY(),
                        paint
                );

            }            //draw game Over when the game is over
            if (isGameOver) {
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setARGB(255, 0, 0, 255);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
            }


            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);


        }
    }


    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;

        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //unregister Sensor listener
        manager.unregisterListener(this);

    }

    public void resume() {

        //when the game is resumed
        manager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
 //       manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getWidth();
        HEIGHT = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getHeight();


        //starting the thread again


        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas));

        //updating the item coordinate with respect to player speed
        bg.setVector(-20);

        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getWidth();
        HEIGHT= BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getHeight();
        gameThread = new Thread(this);
        gameThread.start();
        playing = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int z = 0;
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //stopping the boosting when screen is released

                //int cellY = (int)motionEvent.getY();

                player.stopBoosting();
                break;


            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //boosting the space jet when screen is pressed

                int w = getWidth();
                int h = getHeight();
                int cellX = (int) motionEvent.getX();

                player.setBoosting(cellX,true);
                break;

        }
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
       if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            if (event.values[2] > 0.5f) { // anticlockwise
                player.setBoosting(Math.round(event.values[2]),false);
               // Toast.makeText(this.getContext(),"val:"+event.values[2],Toast.LENGTH_LONG).show();
            } else if (event.values[2] < -0.5f) { // clockwise
                player.setBoosting(Math.round(event.values[2]),false);
              //  Toast.makeText(this.getContext(),"val:"+event.values[2],Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
