package megadroid.drivinggame.model;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import megadroid.drivinggame.R;

/**
 * Class used for generating the music player and sound pool objects used in the game
 */

public class SoundHelper {

    //this object is created for playing music
    private MediaPlayer mMusicPlayer;
    private SoundPool mSoundPool;
    private int  mcrashID;
    private int  mcoinID;
    private int mdiamondID;
    private int moverID;
    private boolean mLoaded;
    private float mVolume;

    /**
     * Constructor method used to generate the Sound pool and assign the different sounds to the object
     * @param activity
     */
    public SoundHelper(Activity activity) {

        //create audio manager for playing sounds
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = actVolume / maxVolume;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
        } else {
            //noinspection deprecation
            mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded = true;
            }
        });

        //different sounds are initialised for the game
        mcrashID = mSoundPool.load(activity.getApplicationContext(), R.raw.car_crash, 1);
        mcoinID = mSoundPool.load(activity.getApplicationContext(), R.raw.coins, 1);
        mdiamondID = mSoundPool.load(activity.getApplicationContext(), R.raw.crystal, 1);
        moverID = mSoundPool.load(activity.getApplicationContext(), R.raw.game_over, 1);
    }

    //car crash sound
    public void CrashSound() {
        if (mLoaded) {
            mSoundPool.play(mcrashID, mVolume, mVolume, 1, 0, 1f);
        }

    }

    //coins collected sound
    public void CoinCollection() {
        if (mLoaded) {
            mSoundPool.play(mcoinID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    //crystal collection sound
    public void diamondCollection() {
        if (mLoaded) {
            mSoundPool.play(mdiamondID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    //gameover sound
    public void gameOver() {
        if (mLoaded) {
            mSoundPool.play(moverID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    //playing continuous music in background with low volume
    public void prepareMusicPlayer(Context context, int musicfile)
    {
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                musicfile);
          //sets the volume of the music
        mMusicPlayer.setVolume(.5f,.5f);
        mMusicPlayer.setLooping(true);
    }

    //playing continuous music in background with higher volume
    public void prepareMusicPlayer2(Context context, int musicfile)
    {
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                musicfile);
        //sets the volume of the music
        mMusicPlayer.setVolume(1f,1f);
        mMusicPlayer.setLooping(true);
    }

    //playing music in background only once
    public void prepareMusicPlayer3(Context context, int musicfile)
    {
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                musicfile);
        //sets the volume of the music
        mMusicPlayer.setVolume(1f,1f);
        mMusicPlayer.setLooping(false);
    }

    //to play the music
    public void playMusic()
    {
        if(mMusicPlayer != null)
        {
            mMusicPlayer.start();
        }
    }

    //to pause the music
    public void pauseMusic()
    {
        if(mMusicPlayer != null && mMusicPlayer.isPlaying())
        {
            mMusicPlayer.pause();

        }
    }

    //Method to release the sound pool and music player objects
    public void stopMusic(){

        if(mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        if(mMusicPlayer != null) {
            mMusicPlayer.release();
            mMusicPlayer = null;
        }

    }

    //getter method to get th esound pool object
    public SoundPool getmSoundPool() {
        return mSoundPool;
    }

    //getter method to get the music player object
    public MediaPlayer getmMusicPlayer() {
        return mMusicPlayer;
    }
}
