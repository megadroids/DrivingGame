package megadroid.drivinggame.view;

import android.app.Activity;
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
import megadroid.drivinggame.controller.Generator;
import megadroid.drivinggame.model.Boom;
import megadroid.drivinggame.model.Items;
import megadroid.drivinggame.model.Obstacles;
import megadroid.drivinggame.model.SoundHelper;
import megadroid.drivinggame.model.SpaceBackground;
import megadroid.drivinggame.model.SpacePlayer;
import megadroid.drivinggame.model.Star;

/**
 * Created by megadroids.
 */

public class SpaceGameView extends SurfaceView implements Runnable,SensorEventListener {

    public static float xAccel, xVel = 0.0f;
    public static float yAccel, yVel = 0.0f;


    private SensorManager sensorManager;

    //private SensorManager manager;
    //private Sensor accelerometer;
    //private Sensor gyroscopeSensor;
    // private float xAcceleration,yAcceleration,zAcceleration;

    //used to count when the crystal item will be released
    private int counter;

    //music spacePlayer
    private SoundHelper msoundHelper;

    //properties of the background image and instantiation of the background class
    private Items[] item;
    private Items[] item1;
    private Items[] item2;
    //Adding 3 items you
    private int itemCount = 2;
    private int itemCount1 =2;
    private ArrayList<Star> stars = new ArrayList<Star>();

    //Controls speed of the background scroll
    // public static final int MOVESPEED = -10;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    volatile int playingCounter = 0;

    //the game thread
    private Thread gameThread = null;

    //adding the spacePlayer to this class
    private SpacePlayer spacePlayer;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //created a reference of the class Friend
    private Obstacles obstacles;
    private Obstacles obstacles2;
    private Obstacles obstacles3;

    //an indicator if the game is Over
    private boolean isGameOver;

    //defining a boom object to display blast
    private Boom boom;

    //properties of the background image and instantiation of the background class
    public static float WIDTH;//640;
    public static float HEIGHT;//1440;
    private SpaceBackground bg2;

    private int screenX;
    private int screenY;
    private int score;
    private int highScore;
    private int points;
    private Generator generator;


    //Class constructor
    public SpaceGameView(Context context, int screenX, int screenY) {
        super(context);

        generator = new Generator(context);
        //setting the score to 0 initially
        score = 0;
        points=0;
        //get JSON values
        highScore = generator.getHighScore();
        int selectedCar = generator.getSelectedCar();

        //play the music
        msoundHelper = new SoundHelper((Activity)this.getContext());
        msoundHelper.prepareMusicPlayer((Activity)this.getContext(),R.raw.main_game1);
        msoundHelper.playMusic();


        //declaring Sensor Manager and sensor type
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // gyroscopeSensor =  manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


// Register the listener
        //manager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



        //initializing spacePlayer object
        //this time also passing screen size to spacePlayer constructor
        spacePlayer = new SpacePlayer(context, screenX, screenY,selectedCar);

        int starNums = 500;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);

        }


        Bitmap bitmapCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_gold);
        Bitmap bitmapCrystal = BitmapFactory.decodeResource(context.getResources(), R.drawable.crystal);

        //coins on the left side
        item = new Items[itemCount];
        for (int j = 0; j < itemCount; j++) {

            item[j] = new Items(this.getContext(), screenX/2-200 , screenY, bitmapCoin);
        }

        //coins on the right side
        item1 = new Items[itemCount];
        for (int k = 0; k < itemCount; k++) {

            item1[k] = new Items(this.getContext(), screenX/2+100 , screenY, bitmapCoin);
        }

        //the crystal item
        //coins on the right side
        item2 = new Items[itemCount1];
        for (int m = 0; m < itemCount1; m++) {
            item2[m] = new Items(this.getContext(), screenX * 2 - 400, screenY, bitmapCrystal);
        }

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //initializing boom object
        boom = new Boom(context);

        //initializing the Friend class object
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.def_car);
        Bitmap bitmapcar = BitmapFactory.decodeResource(this.getResources(), R.drawable.racecar);
        Bitmap bitmapSecond = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);

        obstacles = new Obstacles(this.getContext(), screenX, screenY,bitmap,screenX/2-240,screenX/2);
        obstacles2 = new Obstacles(this.getContext(), screenX, screenY,bitmapcar,screenX/2+100,screenX/2+200);
        obstacles3 = new Obstacles(this.getContext(), screenX, screenY,bitmapSecond,screenX/2+20,screenX/2+280);

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


        //increament counter for the release of the crystal
        if(playingCounter%50==0){
            counter++;
        }

        //incrementing score as time passes
        if(playingCounter%22==0) {
            score++;
        }

        //updating spacePlayer position
        spacePlayer.update();

        //update the background to move
        bg2.update(playingCounter);

        // update the stars
        for (Star s : stars) {
            s.update(spacePlayer.getSpeed());

        }

        for (int i = 0; i < itemCount; i++) {

            item[i].update(spacePlayer.getSpeed());

            //if collision occurrs with spacePlayer
            if (Rect.intersects(spacePlayer.getDetectCollision(), item[i].getDetectCollision())) {
                //moving item outside the topedge
                item[i].setY(-200);
                points++;
                msoundHelper.CoinCollection();

            }

        }

        for (int j = 0; j < itemCount; j++) {

            item1[j].update(spacePlayer.getSpeed());

            //if collision occurrs with spacePlayer
            if (Rect.intersects(spacePlayer.getDetectCollision(), item1[j].getDetectCollision())) {
                //moving item outside the topedge
                item1[j].setY(-200);
                points++;
                msoundHelper.CoinCollection();
            }
        }

        for (int m = 0; m < itemCount1; m++) {

            item2[m].update(spacePlayer.getSpeed());

            //if collision occurrs with spacePlayer
            if (Rect.intersects(spacePlayer.getDetectCollision(), item2[m].getDetectCollision())) {
                //moving item outside the topedge
                item2[m].setY(-200);
                points+=5;
                msoundHelper.CoinCollection();
            }
        }

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);


        //updating the friend ships coordinates


        //checking for a collision between spacePlayer and a racecar
        if (playingCounter > 20 && playingCounter < 1000) {
            obstacles2.update(spacePlayer.getSpeed()+10);

            if (Rect.intersects(spacePlayer.getDetectCollision(), obstacles2.getDetectCollision())) {
                gameOver(obstacles2);
            }
        }


        //checking for a collision between spacePlayer and a car
        if (playingCounter > 100) {
            obstacles.update(spacePlayer.getSpeed());

            if (Rect.intersects(spacePlayer.getDetectCollision(), obstacles.getDetectCollision())) {

                gameOver(obstacles);
            }
        }

        //checking for a collision between spacePlayer and a enemy
        if (playingCounter > 1000) {
            obstacles3.update(spacePlayer.getSpeed()+15);

            if (Rect.intersects(spacePlayer.getDetectCollision(), obstacles3.getDetectCollision())) {
                gameOver(obstacles3);
            }
        }

    }

    private void gameOver(Obstacles obstacles){
        //displaying the boom at the collision
        boom.setX(obstacles.getX());
        boom.setY(obstacles.getY());
        //setting playing false to stop the game
        playing = false;
        //setting the isGameOver true as the game is over
        isGameOver = true;
        //get the highscore
        if(highScore< score){
            highScore = score;
        }

        //crash sound
        msoundHelper.CrashSound();

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
                bg2.draw(canvas);
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
            if (counter%30 ==0 ) {
                //drawing the items
                for (int i = 0; i < itemCount1; i++) {
                    canvas.drawBitmap(
                            item2[i].getBitmap(),
                            item2[i].getX(),
                            item2[i].getY(),
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

            //drawing the score on the game screen
            paint.setTextSize(50);
            canvas.drawText("Score:"+score,40,50,paint);


            //drawing the points on the game screen
            paint.setTextSize(50);
            canvas.drawText("Points : "+points,screenX-300,50,paint);


            //Drawing the spacePlayer
            canvas.drawBitmap(
                    spacePlayer.getBitmap(),
                    spacePlayer.getX(),
                    spacePlayer.getY(),
                    paint);

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing obstacles image
            //draw white car
            if (playingCounter > 120) {

                canvas.drawBitmap(

                        obstacles.getBitmap(),
                        obstacles.getX(),
                        obstacles.getY(),
                        paint
                );
            }
            //draw race car
            if (playingCounter > 20 && playingCounter < 1000) {

                canvas.drawBitmap(

                        obstacles2.getBitmap(),
                        obstacles2.getX(),
                        obstacles2.getY(),
                        paint
                );

            }

            //draw red car
            if (playingCounter > 1000) {

                canvas.drawBitmap(

                        obstacles3.getBitmap(),
                        obstacles3.getX(),
                        obstacles3.getY(),
                        paint
                );

            }

            //draw game Over when the game is over
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
        sensorManager.unregisterListener(this);
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
        // manager.unregisterListener(this);

        //write the score and points to JSON
        generator.writeJson(this.getContext(),highScore,points);

        //stop the music
        msoundHelper.pauseMusic();

    }

    public void resume() {

        //when the game is resumed
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        //       manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.bg2).getWidth();
        HEIGHT = BitmapFactory.decodeResource(getResources(), R.drawable.bg2).getHeight();

        int selectedTheme =generator.getSelectedTheme();
        //starting the thread again
        bg2 = new SpaceBackground(BitmapFactory.decodeResource(getResources(), selectedTheme));

        //updating the item coordinate with respect to spacePlayer speed
        bg2.setVector(-25);

        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.bg2).getWidth();
        HEIGHT = BitmapFactory.decodeResource(getResources(), R.drawable.bg2).getHeight();
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

                spacePlayer.stopBoosting();
                break;


            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //boosting the space jet when screen is pressed

                int w = getWidth();
                int h = getHeight();
                int cellX = (int) motionEvent.getX();

                spacePlayer.setBoosting(cellX, true);
                break;

        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = event.values[0];
            yAccel = -event.values[1];
            spacePlayer.updateTilt();

            /** if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

             if (event.values[2] > 0.5f) { // anticlockwise
             spacePlayer.setBoosting(Math.round(event.values[2]),false);
             // Toast.makeText(this.getContext(),"val:"+event.values[2],Toast.LENGTH_LONG).show();
             } else if (event.values[2] < -0.5f) { // clockwise
             spacePlayer.setBoosting(Math.round(event.values[2]),false);
             //  Toast.makeText(this.getContext(),"val:"+event.values[2],Toast.LENGTH_LONG).show();
             }
             }**/

        }
    }

    public void onAccuracyChanged (Sensor sensor,int i){

    }


}