package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;


public class SoundPlayer {

    private static SoundPool soundPool;
    private static int pointSound;
    private static int overSound;
    private static int hitSound;
    MediaPlayer backgroundMusic;
    private static int countSound;
    private static int startSound;
    private static int jumpSound;
    MediaPlayer goldenCroissant;
    MediaPlayer startMusic;
    MediaPlayer goldenHit;

    public SoundPlayer(Context context){

        //SoundPool (int maxStreams, int streamType, int srcQuality)
        soundPool= new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        backgroundMusic=MediaPlayer.create(context,R.raw.background);
        goldenCroissant=MediaPlayer.create(context,R.raw.angel);
        startMusic=MediaPlayer.create(context,R.raw.menu);
        goldenHit=MediaPlayer.create(context,R.raw.hallelujah);
        pointSound= soundPool.load(context, R.raw.hit, 1);
        overSound= soundPool.load(context, R.raw.over, 1);
        hitSound= soundPool.load(context, R.raw.oof, 1);
        countSound= soundPool.load(context,R.raw.count,1);
        startSound= soundPool.load(context,R.raw.start,1);
        jumpSound= soundPool.load(context,R.raw.jump,1);
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

    public void playCountSound(){

        soundPool.play(countSound,1.0f,1.0f,1,0,1);
    }

    public void playStartSound(){

        soundPool.play(startSound,1.0f,1.0f,1,0,1);
    }

    public void playJumpSound(){

        soundPool.play(jumpSound,1.0f,1.0f,1,0,1);
    }

    public void playAngelSound(){
        goldenCroissant.start();
    }

    public void stopAngelSound(){
        goldenCroissant.stop();
    }

    public void playStartMusic(){
        startMusic.start();
    }

    public void stopStartMusic(){
        startMusic.stop();
    }

    public void startGoldenHit(){

        goldenHit.start();
    }

    public void stopGoldenHit(){

        goldenHit.stop();
    }

}
