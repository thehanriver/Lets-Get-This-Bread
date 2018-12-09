package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int pointSound;
    private static int overSound;
    private static int hitSound;
    private static int backgroundMusic;


    public SoundPlayer(Context context){

        //SoundPool (int maxStreams, int streamType, int srcQuality)
        soundPool= new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        pointSound= soundPool.load(context, R.raw.hit, 1);
        overSound= soundPool.load(context, R.raw.over, 1);
        hitSound= soundPool.load(context, R.raw.oof, 1);
        backgroundMusic= soundPool.load(context, R.raw.background, 1);
    }

    public void playPointSound() {

        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(pointSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playOverSound(){

        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitSound(){

        soundPool.play(hitSound, 1.0f, 1.0f, 1,0,1.0f);
    }

    public void playBackgroundMusic(){

        soundPool.play(backgroundMusic,1.0f,1.0f,1,10,1.0f);
    }

    public void pauseBackgroundMusic(){

        soundPool.autoPause();
    }

    public void resumeBackgroundMusic(){

        soundPool.autoResume();
    }

    public void stopBackgroundMusic(){

        soundPool.stop(backgroundMusic);
    }
}
