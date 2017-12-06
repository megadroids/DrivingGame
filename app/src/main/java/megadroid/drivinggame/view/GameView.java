package megadroid.drivinggame.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import megadroid.drivinggame.R;
import megadroid.drivinggame.controller.Generator;
import megadroid.drivinggame.model.Background;
import megadroid.drivinggame.model.Boom;
import megadroid.drivinggame.model.Items;
import megadroid.drivinggame.model.Obstacles;
import megadroid.drivinggame.model.Player;
import megadroid.drivinggame.model.SoundHelper;
import megadroid.drivinggame.model.Star;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by megadroids.
 */

public class GameView extends SurfaceView implements Runnable,SensorEventListener {

    public static float xAccel, xVel = 0.0f;
    public static float yAccel, yVel = 0.0f;

    private SensorManager sensorManager;

    //private SensorManager manager;
    //private Sensor accelerometer;
    //private Sensor gyroscopeSensor;
    // private float xAcceleration,yAcceleration,zAcceleration;

    //music player
    private SoundHelper msoundHelper;
    private Random random = new Random();

    //properties of the background image and instantiation of the background class
    private Items[] item;
    private Items[] item1;
    //Adding 3 items you
    private int itemCount = 2;
    private ArrayList<Star> stars = new ArrayList<Star>();

    //Controls speed of the background scroll
    // public static final int MOVESPEED = -10;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    volatile int playingCounter = 0;

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
    private Obstacles obstacles3;

    //an indicator if the game is Over
    private boolean isGameOver;

    //defining a boom object to display blast
    private Boom boom;

    //properties of the background image and instantiation of the background class
    public static float WIDTH;//640;
    public static float HEIGHT;//1440;
    private Background bg;

    private int screenX;
    private int screenY;
    private int score;
    private int highScore;
    private int points;
    private Generator generator;
    private int muteFlag;
    private Bitmap pauseButton;
    private boolean pausePop;


    //Class constructor
    public GameView(Context context, int screenX, int screenY, int muteFlag) {
        super(context);

        this.muteFlag = muteFlag;

        generator = new Generator(context);
        //setting the score to 0 initially
        score = 0;
        points= generator.getPoints();
        //get JSON values
        highScore = generator.getHighScore();
        int selectedCar = generator.getSelectedCar();

        //play the music
        msoundHelper = new SoundHelper((Activity)this.getContext());
        msoundHelper.prepareMusicPlayer((Activity)this.getContext(),randomMainMusic());
        if(muteFlag == 0) {
            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }


        //declaring Sensor Manager and sensor type
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // gyroscopeSensor =  manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


// Register the listener
        //manager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



        //initializing player object
        //this time also passing screen size to player constructor
        player = new Player(context, screenX, screenY,selectedCar);

        int starNums = 800;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);

        }

        pauseButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_pause);
        Bitmap bitmapCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_gold);
        //Bitmap bitmapCrystal = BitmapFactory.decodeResource(context.getResources(), R.drawable.crystal);
        //coins on the left side
        item = new Items[itemCount];
        for (int j = 0; j < itemCount; j++) {

            item[j] = new Items(this.getContext(), screenX * 2 - 450, screenY, bitmapCoin);
        }

        //coins on the right side
        item1 = new Items[itemCount];
        for (int k = 0; k < itemCount; k++) {

            item1[k] = new Items(this.getContext(), screenX * 3 - 150, screenY, bitmapCoin);
        }

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //initializing boom object
        boom = new Boom(context);

        //initializing the Friend class object
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.car);
        Bitmap bitmapcar = BitmapFactory.decodeResource(this.getResources(), R.drawable.racecar);
        Bitmap bitmapSecond = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);

        /*obstacles = new Obstacles(this.getContext(), screenX, screenY,bitmap,screenX/2-300,screenX/2);
        obstacles2 = new Obstacles(this.getContext(), screenX, screenY,bitmapcar,screenX/2+120,screenX/2+200);
        obstacles3 = new Obstacles(this.getContext(), screenX, screenY,bitmapSecond,screenX/2+20,screenX/2+280);
*/
        obstacles = new Obstacles(this.getContext(), screenX, screenY,bitmap,50,screenX-30);
        obstacles2 = new Obstacles(this.getContext(), screenX, screenY,bitmapcar,50,screenX-30);
        obstacles3 = new Obstacles(this.getContext(), screenX, screenY,bitmapSecond,50,screenX-30);

        //set new position of cars if they overlap
        //if((int)obstacles.getX() == (int)obstacles2.getX()){
        if((((int)obstacles.getX()+(obstacles.getBitmap().getWidth()/2)) < ((int)obstacles2.getX()+(obstacles2.getBitmap().getWidth()/2)))
                && (((int)obstacles.getX()+(obstacles.getBitmap().getWidth()/2)) > ((int)obstacles2.getX()-(obstacles2.getBitmap().getWidth()/2))))
        {
            int newX = obstacles.getX()+200;
            if(newX > screenX-30){
                newX = obstacles.getX()-200;
            }
            obstacles2.setX(newX);
        }

        //set new position of cars if they overlap
        //if((int)obstacles.getX() == (int)obstacles3.getX()){
        if((((int)obstacles.getX()+(obstacles.getBitmap().getWidth()/2)) < ((int)obstacles3.getX()+(obstacles3.getBitmap().getWidth()/2)))
                && (((int)obstacles.getX()+(obstacles.getBitmap().getWidth()/2)) > ((int)obstacles3.getX()-(obstacles3.getBitmap().getWidth()/2))))
        {
            int newX = obstacles.getX()+200;
            if(newX > screenX-30){
                newX = obstacles.getX()-200;
            }
            obstacles3.setX(newX);
        }


        isGameOver = false;

        this.screenX = screenX;
        this.screenY = screenY;

        pausePop = false;


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

        //incrementing score as time passes
        if(playingCounter%22==0) {
            score++;
        }

        //updating player position
        player.update();

        //update the background to move
        bg.update(playingCounter);

        // update the stars
        for (Star s : stars) {
            s.update(player.getSpeed());

        }

        if (playingCounter > 100) {
            for (int i = 0; i < itemCount; i++) {

                item[i].update(player.getSpeed());

                //if collision occurrs with player
                if (Rect.intersects(player.getDetectCollision(), item[i].getDetectCollision())) {
                    //moving item outside the topedge
                    item[i].setY(-200);
                    points++;
                    if (muteFlag == 0) {
                        msoundHelper.CoinCollection();
                    } else {
                        msoundHelper.pauseMusic();
                    }


                }

            }
        }

        if (playingCounter > 200) {

            for (int j = 0; j < itemCount; j++) {

                item1[j].update(player.getSpeed());

                //if collision occurrs with player
                if (Rect.intersects(player.getDetectCollision(), item1[j].getDetectCollision())) {
                    //moving item outside the topedge
                    item1[j].setY(-200);
                    points++;
                    if (muteFlag == 0) {
                        msoundHelper.CoinCollection();
                    } else {
                        msoundHelper.pauseMusic();
                    }

                }
            }
        }
        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);


        //updating the friend ships coordinates


        //checking for a collision between player and a racecar
        if (playingCounter > 20 && playingCounter < 1000) {
            obstacles2.update(player.getSpeed()+10);
            if (Rect.intersects(player.getDetectCollision(), obstacles2.getDetectCollision())) {
                gameOver(obstacles2);
            }
        }


        //checking for a collision between player and a car
        if (playingCounter > 180) {
            obstacles.update(player.getSpeed());
            if (Rect.intersects(player.getDetectCollision(), obstacles.getDetectCollision())) {

                gameOver(obstacles);
            }
        }

        //checking for a collision between player and a enemy
        if (playingCounter > 1000) {

            obstacles3.update(player.getSpeed()+15);
            if (Rect.intersects(player.getDetectCollision(), obstacles3.getDetectCollision())) {
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
        if(muteFlag == 0) {
            msoundHelper.CrashSound();
        }else
        {
            msoundHelper.pauseMusic();
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
            //draw white car
            if (playingCounter > 180) {

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

            // create a rectangle that we'll draw later
            Rect rectangle = new Rect(0, 0, screenX, 90 );
            paint.setColor(Color.BLACK);
            canvas.drawRect(rectangle, paint);


            //drawing the score on the game screen
            paint.setColor(Color.WHITE);
            paint.setTextSize(45);
            canvas.drawText("Score: " + score, screenX - 310, 50, paint);

            //drawing the points on the game screen
            paint.setColor(Color.WHITE);
            paint.setTextSize(45);
            canvas.drawBitmap(item1[0].getBitmap(),20,0,paint);
            canvas.drawText(": " + points, item1[0].getBitmap().getWidth()+20, 50, paint);

            //pause button
            canvas.drawBitmap(

                    pauseButton,
                    screenX-pauseButton.getWidth(),
                    0,
                    paint
            );


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


        pausePop = false;

        //when the game is resumed
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        //       manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if(muteFlag == 0) {
            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }

        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getWidth();
        HEIGHT = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getHeight();

        int selectedTheme =generator.getSelectedTheme();
        //starting the thread again
        bg = new Background(BitmapFactory.decodeResource(getResources(), selectedTheme));

        //updating the item coordinate with respect to player speed
        bg.setVector(-25);

        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getWidth();
        HEIGHT= BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getHeight();

        //stop the music
        msoundHelper.playMusic();

        gameThread = new Thread(this);
        gameThread.start();
        playing = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if((motionEvent.getX(0)>=screenX - pauseButton.getWidth()) &&
                (motionEvent.getY(0)>=0) &&
                ( motionEvent.getX(0)<=screenX) &&
                (motionEvent.getY(0)<=pauseButton.getHeight()))
        {
            //pause button selected
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if(!pausePop) {

                    pausePop = true;
                    //write the score and points to JSON
                    //get the highscore
                    if(highScore< score){
                        highScore = score;
                    }
                    generator.writeJson(this.getContext(),highScore,points);

                    getContext().startActivity(new Intent(getContext(), PauseActivity.class));

                }
            }
            // Toast.makeText(this.getContext(),"paused",Toast.LENGTH_SHORT).show();
        }
        else {

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

                    player.setBoosting(cellX, true);
                    break;

            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = event.values[0];
            yAccel = -event.values[1];
            player.updatetilt();

            /** if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

             if (event.values[2] > 0.5f) { // anticlockwise
             player.setBoosting(Math.round(event.values[2]),false);
             // Toast.makeText(this.getContext(),"val:"+event.values[2],Toast.LENGTH_LONG).show();
             } else if (event.values[2] < -0.5f) { // clockwise
             player.setBoosting(Math.round(event.values[2]),false);
             //  Toast.makeText(this.getContext(),"val:"+event.values[2],Toast.LENGTH_LONG).show();
             }
             }**/

        }
    }

    public void onAccuracyChanged (Sensor sensor,int i){

    }

    public int randomMainMusic() {
        int[] randommusic = new int[] {R.raw.main_game1, R.raw.main_game2, R.raw.main_game3};
        int x = random.nextInt(randommusic.length);
        return randommusic[x];
    }
}
