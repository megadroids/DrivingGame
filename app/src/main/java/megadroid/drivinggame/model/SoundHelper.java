package megadroid.drivinggame.model;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import megadroid.drivinggame.R;

/**
 * Created by Shruthi on 27-11-2017.
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

    public SoundHelper(Activity activity) {

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
        mcrashID = mSoundPool.load(activity, R.raw.car_crash, 1);
        mcoinID = mSoundPool.load(activity, R.raw.coins, 1);
        mdiamondID = mSoundPool.load(activity, R.raw.crystal, 1);
        moverID = mSoundPool.load(activity, R.raw.game_over, 1);
    }

    public void CrashSound() {
        if (mLoaded) {
            mSoundPool.play(mcrashID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void CoinCollection() {
        if (mLoaded) {
            mSoundPool.play(mcoinID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void diamondCollection() {
        if (mLoaded) {
            mSoundPool.play(mdiamondID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void gameOver() {
        if (mLoaded) {
            mSoundPool.play(moverID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void prepareMusicPlayer(Context context, int musicfile)
    {
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                musicfile);
          //sets the volume of the music
        mMusicPlayer.setVolume(.5f,.5f);
        mMusicPlayer.setLooping(true);
    }

    public void prepareMusicPlayer2(Context context, int musicfile)
    {
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                musicfile);
        //sets the volume of the music
        mMusicPlayer.setVolume(1f,1f);
        mMusicPlayer.setLooping(true);
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
}
