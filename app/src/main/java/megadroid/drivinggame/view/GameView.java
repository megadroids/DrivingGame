

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
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

/**
 * Created by megadroids.
 */

public class GameView extends SurfaceView implements Runnable,SensorEventListener {

    //Accelerator X value
    public static float xAccel, xVel = 0.0f;


    //Sensor Manager that controls the tilt
    private SensorManager sensorManager;


    //used to count when the crystal item will be released
       private int counter;

    //music player
    private SoundHelper msoundHelper;
    private Random random = new Random();

    //properties of the background image and instantiation of the background class
    private Items[] item;
    private Items[] item1;
    private Items[] item2;
    //Adding 3 items you
    private int itemCount = 2;
    private int itemCount1 =1;
    private ArrayList<Star> stars = new ArrayList<Star>();

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
    private Obstacles obstacles4;

    //an indicator if the game is Over
    private boolean isGameOver;

    //defining a boom object to display blast
    private Boom boom;

    //properties of the background image and instantiation of the background class
    public static float WIDTH;//640;
    public static float HEIGHT;//1440;
    private Background bg;

    //properties to calculate score
    private int screenX;
    private int screenY;
    private int score;
    private int highScore;
    private int points;

    private Generator generator;
    private int muteFlag;

    //pause button properties
    private Bitmap pauseButton;
    private boolean pausePop;

    private int bgSpeed;
    private boolean highscorebeaten;
    private int prevMusic;

    //Class constructor
    public GameView(Context context, int screenX, int screenY, int muteFlag) {
        super(context);

        this.muteFlag = muteFlag;

        //background speed
        bgSpeed = -25;
        generator = new Generator(context);
        //setting the score to 0 initially
        score = 0;
        points= generator.getPoints();
        //get JSON values
        highScore = generator.getHighScore();
        int selectedCar = generator.getSelectedCar();

        //play the music
        msoundHelper = new SoundHelper((Activity)this.getContext());
        prevMusic = randomMainMusic();
        msoundHelper.prepareMusicPlayer((Activity)this.getContext(),prevMusic);
        if(muteFlag == 0) {
            msoundHelper.playMusic();
        }else
        {
            msoundHelper.pauseMusic();
        }


        //declaring Sensor Manager and sensor type
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);


        //initializing player object
        //this time also passing screen size to player constructor
        player = new Player(context, screenX, screenY,selectedCar);

        int starNums = 1000;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);

        }

        pauseButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_pause);
        Bitmap bitmapCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_gold);
        Bitmap bitmapCrystal = BitmapFactory.decodeResource(context.getResources(), R.drawable.crystal);

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
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), randomObstacleCars());
        Bitmap bitmapcar = BitmapFactory.decodeResource(this.getResources(), randomObstacleCars());
        Bitmap bitmapSecond = BitmapFactory.decodeResource(this.getResources(), randomObstacleCars());
        Bitmap bitmapThird = BitmapFactory.decodeResource(this.getResources(), randomObstacleCars());

        obstacles = new Obstacles(this.getContext(), screenX, screenY,bitmap,220,269);
        obstacles2 = new Obstacles(this.getContext(), screenX, screenY,bitmapcar,720,770);
        obstacles3 = new Obstacles(this.getContext(), screenX, screenY,bitmapSecond,550,600);
        obstacles4 = new Obstacles(this.getContext(), screenX, screenY,bitmapThird,380,430);

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


        //increament counter for the release of the crystal
        if(playingCounter%50==0){
            counter++;
        }

        //incrementing score as time passes
        if(playingCounter%22==0) {
            score++;
        }

        //updating player position
        player.update();

        //update the background to move
        bg.update(playingCounter);

        // update the stars
        for(Star s : stars) {
            s.update(player.getSpeed());

        }

        if (playingCounter > 40) {
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

        if (playingCounter > 150) {

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

        if (playingCounter > 500 ) {
            for (int m = 0; m < itemCount1; m++) {

                item2[m].update(player.getSpeed());

                //if collision occurrs with player
                if (Rect.intersects(player.getDetectCollision(), item2[m].getDetectCollision())) {
                    //moving item outside the topedge
                    item2[m].setY(-200);
                    points += 5;
                    msoundHelper.CoinCollection();
                }
            }
        }

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);


        //updating the friend ships coordinates


        //checking for a collision between player and a racecar.  /**&& playingCounter < 1000*/
        Random generator = new Random();
        int increaseObstacleSpeed = generator.nextInt(5) + 15;
        if (playingCounter > 20) {
            obstacles2.update(player.getSpeed()+increaseObstacleSpeed);
            if (Rect.intersects(player.getDetectCollision(), obstacles2.getDetectCollision())) {
                gameOver(obstacles2);
            }
        }

        //checking for a collision between player and a car
        if (playingCounter > 260 && playingCounter < 1000) {
            obstacles4.update(player.getSpeed()+increaseObstacleSpeed);
            if (Rect.intersects(player.getDetectCollision(), obstacles4.getDetectCollision())) {

                gameOver(obstacles4);
            }
        }


        //checking for a collision between player and a car
        if (playingCounter > 180) {
            obstacles.update(player.getSpeed()+increaseObstacleSpeed);
            if (Rect.intersects(player.getDetectCollision(), obstacles.getDetectCollision())) {

                gameOver(obstacles);
            }
        }

        //checking for a collision between player and a enemy
        if (playingCounter > 1000) {

            obstacles3.update(player.getSpeed()+increaseObstacleSpeed);
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
        if(highScore < score){
            highScore = score;
            highscorebeaten = true;
        }
        else {
            highscorebeaten = false;
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

            if (playingCounter > 40) {
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

            if (playingCounter > 150) {
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
            if (playingCounter > 500 ) {
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
            if (playingCounter > 20) {

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
            //draw 4th obstacle car
            if (playingCounter > 260) {

                canvas.drawBitmap(

                        obstacles4.getBitmap(),
                        obstacles4.getX(),
                        obstacles4.getY(),
                        paint
                );


            }



            // create a rectangle that we'll draw later
            RectF rectangle = new RectF(0, 0, screenX, screenY/20);
            paint.setColor(Color.BLACK);
            canvas.drawRect(rectangle, paint);


            //drawing the score on the game screen
            paint.setColor(Color.WHITE);
            paint.setTextSize(45);
            canvas.drawText("Score: " + score, screenX/2+150, 50, paint);

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
            /*    paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setARGB(255, 0, 0, 255);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
            */
                Intent gameover = new Intent(getContext(), GameOverActivity.class);
                gameover.putExtra("highscorebeaten",highscorebeaten);
                gameover.putExtra("muteFlag",muteFlag);
                getContext().startActivity(gameover);
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

        bgSpeed = bg.getVector();
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

        if(isGameOver){
            if(muteFlag == 0) {
                //stop music when game is over
                //msoundHelper.CrashSound();
                msoundHelper.pauseMusic();
                msoundHelper.stopMusic();
                msoundHelper.prepareMusicPlayer3(this.getContext(),R.raw.car_crash);
                    msoundHelper.playMusic();

                //stop the music
                msoundHelper.getmMusicPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        // Code to start the next audio in the sequence
                        msoundHelper.pauseMusic();
                        msoundHelper.stopMusic();
                        msoundHelper = null;

                    }
                });

            }else
            {
                //stop music when mute is ON
                msoundHelper.pauseMusic();
                msoundHelper.stopMusic();
                msoundHelper = null;
            }

        }
        else {
            //stop music when going to Pause screen
            msoundHelper.pauseMusic();
            msoundHelper.stopMusic();
            msoundHelper = null;
        }

    }

    public void resume() {


        pausePop = false;

        //when the game is resumed
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        //       manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if(msoundHelper == null){
            msoundHelper = new SoundHelper((Activity)this.getContext());
            msoundHelper.prepareMusicPlayer((Activity)this.getContext(),prevMusic);
        }

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
        bg.setVector(bgSpeed);

        WIDTH = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getWidth();
        HEIGHT= BitmapFactory.decodeResource(getResources(), R.drawable.backgroundcanvas).getHeight();

        //stop the music
        //msoundHelper.playMusic();

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

                    Intent pauseIntent = new Intent(getContext(), PauseActivity.class);
                    pauseIntent.putExtra("muteFlag",muteFlag);
                    getContext().startActivity(pauseIntent);

                    //getContext().startActivity(new Intent(getContext(), PauseActivity.class));

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
            player.updatetilt();


        }
    }

    public void onAccuracyChanged (Sensor sensor,int i){

    }

    public int randomMainMusic() {


        //Array to hold car obstacles
        int[] randommusic = new int[] {R.raw.main_game1, R.raw.main_game2, R.raw.main_game3};
        int x = random.nextInt(randommusic.length);
        return randommusic[x];
    }

    public int randomObstacleCars() {
        int[] randomObstacleCars = new int[] {R.drawable.car_obst_0, R.drawable.car_obst_1, R.drawable.car_obst_2, R.drawable.car_obst_3, R.drawable.car_obst_4, R.drawable.car_obst_5,R.drawable.car_obst_6};
        int x = random.nextInt(randomObstacleCars.length);
        return randomObstacleCars[x];
    }
}
