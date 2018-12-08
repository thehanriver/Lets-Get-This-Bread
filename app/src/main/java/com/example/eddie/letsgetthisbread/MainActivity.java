package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {









    public class OrientationData implements SensorEventListener {
        private SensorManager manager;
        private Sensor accelerometer;
        private Sensor magnometer;

        private float[] accelOutput;
        private float[] magOutput;

        private float[] orientation = new float[3];
        public float[] getOrientation(){
            return orientation;
        }

        private float[] startOrientation = null;
        public float[] getStartOrientation(){
            return startOrientation;
        }
        public void newGame() {
            startOrientation = null;
        }
        public OrientationData() {
            manager = (SensorManager)getSystemService(SENSOR_SERVICE);
            accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        public void register(){
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
        }
        public void pause(){
            manager.unregisterListener(this);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){

        }
        public void onSensorChanged(SensorEvent event){
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelOutput = event.values;
            else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magOutput = event.values;
            if(accelOutput != null && magOutput != null) {
                float[] R = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
                if(success){
                    SensorManager.getOrientation(R, orientation);
                    if(startOrientation == null){
                        startOrientation = new float[orientation.length];
                        System.arraycopy(orientation,0,startOrientation,0,orientation.length);
                    }
                }
            }
        }
    }

















    // Initialize View objects in layout

	    private TextView scoreboard;
	    private TextView lives;
	    private TextView start;
	    private TextView left;
	    private TextView right;
	    private TextView jump;
	    private TextView debug;
	    private TextView countdown;
	    private ImageView chair;
	    private ImageView character;
	    private ImageView cutlery;
	    private ImageView knife;
	    private ImageView pigeon;

    // Initialize variables for dimensions in layout
        private int frameHeight;
        private int frameWidth;
        private int screenHeight;
        private int character_width;
        private int character_height;
        private int knifeWidth;
        private int cutleryWidth;
        private int pigeonWidth;
        private int chairWidth;
        private int chairHeight;

    // Positions of sprites
        private int characterX;
        private int characterY;
        private int cutleryX;
        private int cutleryY;
        private int knifeX;
        private int knifeY;
        private int pigeonX;
        private int pigeonY;
        private int chairX;
        private int chairY;

    // Intialize classes
        private Handler handler = new Handler();
        private Timer timer = new Timer();

    // Status check

        private boolean start_flag = false;
        private boolean pause_flag = false;
        private boolean left_flag = false;
        private boolean right_flag = false;
        private boolean chair_flag = false;
    	private boolean jump_flag = false;

    // Counter
        private int score = 0;
        private int currentscore;
        private int healthCounter = 3;

    // Button
        private ImageButton pauseButton;

    // Difficulty Multiplier
        private float speed_multiplier = 1;

        private OrientationData orientationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Runs first time on activity startup, inflates layout of MainActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets saved dimensions of sprites from xml file: dimension
        Resources res = getResources();
        knifeWidth = (int)(res.getDimension(R.dimen.knife));
        pigeonWidth = (int)(res.getDimension(R.dimen.pigeon));
        cutleryWidth = (int)(res.getDimension(R.dimen.cutlery));

        // Assign View objects
        //Characters initialized
        character = findViewById(R.id.character);
        cutlery = findViewById(R.id.cutlery);
        knife = findViewById(R.id.knife);
        pigeon = findViewById(R.id.pigeon);
        chair = findViewById(R.id.chair);

        // UI initialized
        scoreboard = findViewById(R.id.scoreboard);
        start = findViewById(R.id.start);
        lives = findViewById(R.id.lives);
        countdown = findViewById(R.id.countdown);
        pauseButton = findViewById(R.id.pause);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        jump= findViewById(R.id.jump);
        debug = findViewById(R.id.debug1);

        // On startup, pause button is not clickable and set countdown to tick from 3
        pauseButton.setClickable(false);
        countdown.setText(Integer.toString(3));

        // TODO: offload constants into its own class file file
        // TODO: compatibility for differnt devices
        // Get height of screen for compatibility reasons
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        orientationData = new OrientationData();
        orientationData.register();


        // Set objects below screen when initialized
        knifeY = screenHeight + 40;
        pigeonY = screenHeight + 40;
        cutleryY = screenHeight + 40;
        chairY = screenHeight + 40;

        // Set positions for falling sprites
        cutlery.setX(-40);
        cutlery.setY(cutleryY);
        pigeon.setX(-40);
        pigeon.setY(pigeonY);
        knife.setX(-40);
        knife.setY(knifeY);

        chair.setX(-40);
        chair.setY(chairY);

        // Set scoreboard & live counter
        scoreboard.setText("Score: 0");
        lives.setText(Integer.toString(healthCounter));

        orientationData.newGame();
    }

    // Input X coordinate of falling sprites and a string key, ie "knife", to return if knife is in the x range of other falling sprites
    public boolean avoidStack(String key, int kx, int cx, int px) {
        boolean result = false;
        switch (key) {
            case "pigeon":
                result = (px >= kx && px <= kx + knifeWidth) || (px + pigeonWidth >= kx && px + pigeonWidth <= kx + knifeWidth) && (px >= cx && px <= cx + cutleryWidth) || (px + pigeonWidth >= cx && px + pigeonWidth <= cx + cutleryWidth);
                break;
            case "cutlery":
                result = (cx >= px && cx <= px + pigeonWidth) || (cx + cutleryWidth >= px && cx + cutleryWidth <= px + pigeonWidth) &&  (cx >= kx && cx <= kx + knifeWidth) || (cx + cutleryWidth >= kx && cx + cutleryWidth <= kx + knifeWidth);
                break;
            case "knife":
                result = (kx >= cx && kx <= cx + cutleryWidth) || (kx + knifeWidth >= cx && kx + knifeWidth <= cx + cutleryWidth) &&  (kx >= cx && kx <= cx + cutleryWidth) || (kx + knifeWidth >= cx && kx + knifeWidth <= cx + cutleryWidth);
                break;
        }
        return result;
    }

    // Input width of a sprite to return a suitable x range for it
    public int shufflePos(int width) {
        return (int) Math.floor(Math.random() * (frameWidth - width));
    }

    // Funtion that will run in a loop to update position of all characters
    public void changePos() {
        // Runs collision check, responds if character touches other characters
        hitCheck();

        // Updates speed of characters as player scores more
        speed_multiplier = difficulty(score);

        // TODO: make it so score has a multiplier for each bread not dropped
        // TODO: add bonus object for achievement system

        // Move obstacle knife
        if (knifeY > frameHeight) // Move knife above screen once knife falls below screen
            knifeY = -100;
        else if (knifeY == -100) { // While still above screen, at -100, do initial shuffle and make this statement false. Required since we need to shuffle at least once
            knifeX = shufflePos(knifeWidth);
            knifeY = -99;
        }
        else if (knifeY < 0 && avoidStack("knife", (int)knife.getX(), (int)cutlery.getX(), (int)pigeon.getX()))  // Shuffle again if knife will collide other objects
            knifeX = shufflePos(knifeWidth);
        else
            knifeY += (12 * speed_multiplier); // Otherwise start falling

        // Set knife coordinates
        knife.setX(knifeX);
        knife.setY(knifeY);

        // Move obstacle cutlery

        if (cutleryY > frameHeight) // Move cutlery above screen once cutlery falls below screen
            cutleryY = -100;
        else if (cutleryY == -100) { // While still above screen, at -100, do initial shuffle and make this statement false
            cutleryX = shufflePos(cutleryWidth);
            cutleryY = -60;
        }
        else if (cutleryY < 0 && avoidStack("cutlery", (int)knife.getX(), (int)cutlery.getX(), (int)pigeon.getX()))  // Shuffle again if cutlery will collide other objects
            cutleryX = shufflePos(cutleryWidth);
        else
            cutleryY += (14 * speed_multiplier); // Otherwise start falling

        cutlery.setX(cutleryX);
        cutlery.setY(cutleryY);

        // Move obstacle knife

        if (pigeonY > frameHeight) // Move pigeon above screen once pigeon falls below screen
            pigeonY = -100;
        else if (pigeonY == -100) { // While still above screen, at -100, do initial shuffle and make this statement false
            pigeonX = shufflePos(pigeonWidth);
            pigeonY = -60;
        }
        else if (pigeonY < 0 && avoidStack("pigeon", (int)knife.getX(), (int)cutlery.getX(), (int)pigeon.getX()))  // Shuffle again if pigeon will collide other objects
            pigeonX = shufflePos(pigeonWidth);
        else
            pigeonY += (12 * speed_multiplier); // Otherwise start falling
        
        pigeon.setX(pigeonX);
        pigeon.setY(pigeonY);

        if (!chair_flag) {
            currentscore = score;
            chairX = shufflePos(chairWidth);
            if ((chairX >= characterX && chairX <= characterX + character_width) || (chairX + chairWidth >= characterX && chairX + chairWidth <= characterX + character_width))
                chairX = shufflePos(chairWidth);
            chair_flag = true;
        }
        else {
            chair.setX(chairX);
            if ((score - currentscore) < 100) {
                chairY -= 10;
            }
            else {
                chairY += 10;
            }


            if (chairY < frameHeight - chairHeight) {
                chairY = frameHeight - chairHeight;
            }
            else if (chairY > frameHeight + chairHeight) {
                chairY = frameHeight + chairHeight;
                chair_flag = false;
            }
        }
        chair.setY(chairY);

        // Move main character
        // TODO: change action flag mechanic into button & tilt

        // Depending on movement flag, move characters

        //motion controlled movement
//        if(orientationData.getOrientation() != null && orientationData.getOrientation() != null) {
//            float pitch = orientationData.getOrientation()[1] - orientationData.getOrientation()[1];
//            float roll = orientationData.getOrientation()[2] - orientationData.getOrientation()[2];
//
//            float xSpeed = 2*roll/1000f;
//            float ySpeed = pitch/1000f;
//
//            characterX += (xSpeed);
//            characterY += (ySpeed);
//        }

        // Make sure character stays inside the boundry of the screen

        if (left_flag)
            characterX -= 20;
        else if (right_flag)
            characterX += 20;

        if ((characterX >= chairX  && characterX <= chairX + chairWidth) || (characterX + character_width >= chairX && characterX + character_width <= chairX + chairWidth)) {
            if (characterX + character_width > chairX && characterX > chairX)
                characterX += 20;
            else if (characterX + character_width < chairX + chairWidth && characterX < chairX + chairWidth )
                characterX -= 20;
        }

        if (characterX < 0)
            characterX = 0;
        else if (characterX > (frameWidth - character_width))
            characterX = frameWidth - character_width;


        debug.setText(Boolean.toString(!((characterX >= chairX  && characterX <= chairX + chairWidth) || (characterX + character_width >= chairX && characterX + character_width <= chairX + chairWidth)))); // temporary to show values of stuff, helpful for debug



        // TODO: add jump mechanic and change characterx
        // Sets the position of the character
        characterY = frameHeight - character_height;

//Make sure character stays inside the boudry of the screen in the vertical direction
        if(characterY<0){
            characterY=0;
        }
        else if(characterY>(frameHeight-character_height)){
            characterY=frameHeight-character_height;
        }

        if(jump_flag){
            characterY-=100;
            if(!jump_flag){
                characterY+=100;
            }
        }

        character.setX(characterX);
        character.setY(characterY);
        // Updates scoreboard
        scoreboard.setText("Score: " + score);
    }

    public void hitCheck() {
        // Boolean statement is basically checking if each corner of an object is inside the character's sprite boundry

        // Collision check for knife, add points
        if ((knifeX + knife.getWidth() >= characterX) && (knifeX <= characterX + character_width) && (knifeY + knife.getHeight() >= characterY) && (knifeY + knife.getHeight() <= frameHeight)) {
            score += 30;
            knifeY = -100;
        }

        // Collision check for pigeon, lose life
        if ((pigeonX + pigeon.getWidth() >= characterX) && (pigeonX <= characterX + character_width) && (pigeonY + pigeon.getHeight() >= characterY) && (pigeonY + pigeon.getHeight() <= frameHeight)) {
            pigeonY = -100;

            // Reduce life counter every time player touches pigeon
            healthCounter -= 1;

            // Once zero, call next activity ResultScreen
            if(healthCounter == 0) {
                lives.setText("X"); // Update Life Counter
                timer.cancel();
                timer = null;

                // Print results
                Intent intent = new Intent(getApplicationContext(), ResultScreen.class);
                intent.putExtra("SCORE", score); // Sends value of score into ResultScreen
                startActivity(intent);
            }
            else {
                lives.setText(Integer.toString(healthCounter)); // Update Life Counter
            }
        }

        // Collision check for cutlery, add points
        if ((cutleryX + cutlery.getWidth() >= characterX) && (cutleryX <= characterX + character_width) && (cutleryY + cutlery.getHeight() >= characterY) && (cutleryY + cutlery.getHeight() <= frameHeight)) {
            score += 50;
            cutleryY = -100;
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!start_flag) { // If its the player's first time touching the screen on MainActivity, call this
            start_flag = true; // Set true so this doesn't get called again

            FrameLayout game_frame = findViewById(R.id.game_frame); // Initialize the FrameLayout that holds most of the objects, get dimensions
            frameHeight = game_frame.getHeight();
            frameWidth = game_frame.getWidth();

            // Initialize character positions and dimensions
            characterX = (int)character.getX();
            characterY = (int)character.getY();
            character_width = character.getWidth();
            character_height = character.getHeight();

            chairWidth = chair.getWidth();
            chairHeight = chair.getHeight();

            // Remove "Tap to Start" display and allow pause to be clickable
            start.setVisibility(View.GONE);
            pauseButton.setClickable(true);

            // Resume calls on the loop function that runs the game
            resume();
        }
        else { // Detects player's finger motion
            if (me.getAction() == MotionEvent.ACTION_DOWN) { // If holding down, check where the player's finger position on the screen
                if (inLeftBoundry(me.getX(),me.getY())) { // If position in Object: left, set left flag true. Will move character left in changePos()
                    left_flag = true;
                }
                if (inRightBoundry(me.getX(),me.getY())) { // Move Right
                    right_flag = true;
                }
                if (inJumpBoundry(me.getX(),me.getY())){ //jump
                    jump_flag = true;
                }
            } else if (me.getAction() == MotionEvent.ACTION_UP) { // If finger leaves screen, return flags to false
                left_flag = false;
                right_flag = false;
                jump_flag = false;
            }
        }

        return true;
    }

    // Disable return key on android phones
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    // Implements pause button
    public void pausePushed(View view) {
        if (!pause_flag) { // Pause flag keeps track of the state of the game
            pause_flag = true;

            // Stop timer and show paused interface
            timer.cancel();
            timer = null;

            // Show PAUSED state
            countdown.setVisibility(View.VISIBLE);
            countdown.setText("PAUSED");
            pauseButton.setAlpha(128);
            // TODO: change pauseButton.setImageDrawable();
        }
        else { // Resume game and reset flag state
            pause_flag = false;
            resume();
        }
    }

    // Function that runs the game loop
    public void resume() { // Before the game starts, there will be a timer buffer that counts down before game starts
        countdown.setVisibility(View.VISIBLE);

        // CountDownTimer counts down from 3 seconds doing actions every second
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                // TODO: add setting button and replace alpha
                // Make pause button invisible and disable interaction
                pauseButton.setAlpha(128);
                pauseButton.setClickable(false);

                // Print time as timer counts down, prints "BREADY?" when timer is down to 1
                int display = (int)(1 + Math.round(millisUntilFinished/1000));
                if (display < 2)
                    countdown.setText("BREADY?");
                else
                    countdown.setText(Integer.toString(display));

            }

            // Resumes game time and things will start moving
            public void onFinish() {
                // Make pause button visible and enable interaction, countdown will disappear
                countdown.setVisibility(View.INVISIBLE);
                pauseButton.setAlpha(255);
                pauseButton.setClickable(true);

                // Resume game timer
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                changePos(); // In charge of update all the sprites as time goes on
                            }
                        });
                    }
                }, 0, 20);
            }
        }.start();
    }

    // TODO: Make boundry check more general
    // Checks if input coordinate is in left/right button also jump. Used to see if user is pressing the "buttons"
    // The buttons are actually images so you cant do call functions
    public boolean inLeftBoundry(float x, float y) {
        return ((x <= left.getX() + left.getWidth()) && (x >= left.getX()) && (y >= left.getY()) && (y <= left.getY() + left.getHeight()));
    }

    public boolean inRightBoundry(float x, float y) {
        return ((x <= right.getX() + right.getWidth()) && (x >= right.getX()) && (y >= right.getY()) && (y <= right.getY() + right.getHeight()));
    }

    public boolean inJumpBoundry(float x,float y){
        return((x<= jump.getX() + jump.getWidth()) && (x>=jump.getX()) && (y>=jump.getY())&&(y<=jump.getY()+jump.getHeight()));
    }
    // Updates the player difficulty by score
    public float difficulty(int PlayerScore) {
        return 1 + (float)PlayerScore/1000; // Start at difficulty 1
    }
}
