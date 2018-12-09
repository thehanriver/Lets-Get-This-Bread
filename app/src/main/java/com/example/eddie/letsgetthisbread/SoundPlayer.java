package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;



public class SoundPlayer {

    private static SoundPool soundPool;
    private static int pointSound;
    private static int overSound;
    private static int hitSound;
    MediaPlayer backgroundMusic;


    public SoundPlayer(Context context){

        //SoundPool (int maxStreams, int streamType, int srcQuality)
        soundPool= new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
         backgroundMusic=MediaPlayer.create(context,R.raw.background);
        pointSound= soundPool.load(context, R.raw.hit, 1);
        overSound= soundPool.load(context, R.raw.over, 1);
        hitSound= soundPool.load(context, R.raw.oof, 1);
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

        backgroundMusic.start();
    }

    public void pauseBackgroundMusic(){

        backgroundMusic.pause();
    }


    public void stopBackgroundMusic(){

        backgroundMusic.stop();
    }
}
