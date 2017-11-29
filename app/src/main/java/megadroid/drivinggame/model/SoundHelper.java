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
    private int mSoundID;
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
        mSoundID = mSoundPool.load(activity, R.raw.explode, 1);
    }

    public void playSound() {
        if (mLoaded) {
            mSoundPool.play(mSoundID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void prepareMusicPlayer(Context context, int musicfile)
    {
        mMusicPlayer = MediaPlayer.create(context.getApplicationContext(),
                musicfile);
 //       mMusicPlayer1 = MediaPlayer.create(context.getApplicationContext(),
   //             R.raw.game_activity);
        mMusicPlayer.setVolume(.5f,.5f);
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

    public void pauseMusic()
    {
        if(mMusicPlayer != null && mMusicPlayer.isPlaying())
        {
            mMusicPlayer.pause();

        }
    }



}
