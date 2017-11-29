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
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import android.view.Surface;


import org.json.JSONException;

import java.util.ArrayList;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.ScoreMonitor;
import megadroid.drivinggame.model.Boom;
import megadroid.drivinggame.model.Items;
import megadroid.drivinggame.model.Obstacles;
import megadroid.drivinggame.model.Player;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by megadroids.
 */

public class GameView extends SurfaceView implements Runnable,SensorEventListener {

    private SensorManager sensorMgr ;
    private Sensor accelerometer;
   // private float xPos, xAccel, xVel = 0.0f;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

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
        sensorMgr = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        //initializing player object
        //this time also passing screen size to player constructor
        player = new Player(context, screenX, screenY);

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
        obstacles2 = new Obstacles(this.getContext(), screenX, screenY,bitmapcar,screenX/2+100,1);

        isGameOver = false;

        this.screenX = screenX;
        this.screenY = screenY;

    }

    @Override
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

        //Update background
        bg.update();

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);


        //updating the friend ships coordinates
        obstacles.update(player.getSpeed());
        obstacles2.update(player.getSpeed()+10);

        //checking for a collision between player and a friend
        if(Rect.intersects(player.getDetectCollision(),obstacles.getDetectCollision())){

            //displaying the boom at the collision
            boom.setX(obstacles.getX());
            boom.setY(obstacles.getY());
            //setting playing false to stop the game
            playing = false;
            //setting the isGameOver true as the game is over
            isGameOver = true;
        }

        if(Rect.intersects(player.getDetectCollision(),obstacles2.getDetectCollision())){

            //displaying the boom at the collision
            boom.setX(obstacles2.getX());
            boom.setY(obstacles2.getY());
            //setting playing false to stop the game
            playing = false;
            //setting the isGameOver true as the game is over
            isGameOver = true;
        }


    }

    private void draw() {
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
                canvas.scale( scaleFactorX,scaleFactorY);
                bg.draw(canvas);
                canvas.restoreToCount(savedState);
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
            canvas.drawBitmap(

                    obstacles.getBitmap(),
                    obstacles.getX(),
                    obstacles.getY(),
                    paint
            );

            canvas.drawBitmap(

                    obstacles2.getBitmap(),
                    obstacles2.getX(),
                    obstacles2.getY(),
                    paint
            );

            //draw game Over when the game is over
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setARGB(255,0,0,255);
                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
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
        }
        //unregister Sensor listener
        sensorMgr.unregisterListener(this);
    }

    public void resume() {
        //when the game is resumed

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas));
        bg.setVector(-45);

        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getWidth();
        HEIGHT = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getHeight();


        //starting the thread again
        playing = true;

        gameThread = new Thread(this);
        gameThread.start();

        sensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int z=0;
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
                int cellX = (int)motionEvent.getX();

                player.setBoosting(cellX);
                break;

        }
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x,y,z;
        switch (getContext().getSystemService(WINDOW_SERVICE).getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                x = sensorEvent.values[0];
                y= sensorEvent.values[1];
                break;
            case Surface.ROTATION_90:
                x= -sensorEvent.values[1];
                y= sensorEvent.values[0];
                break;
            case Surface.ROTATION_180:
                x= -sensorEvent.values[0];
                y = -sensorEvent.values[1];
                break;
            case Surface.ROTATION_270:
                x = sensorEvent.values[1];
                y = -sensorEvent.values[0];
                break;
        }


        //  = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(mAccel > 0.5) {

                Toast.makeText(this.getContext(),"x: "+x,Toast.LENGTH_SHORT).show();
                player.setBoosting(Math.round(x));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}