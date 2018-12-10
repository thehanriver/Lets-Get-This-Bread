package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

        private TextView debug;
	    private TextView scoreboard;
	    private ImageView life1;
	    private ImageView life2;
	    private ImageView life3;
	    private TextView start;
	    private TextView left;
	    private TextView right;
	    private TextView jump;
	    private TextView countdown;
	    private ImageView chair;
	    private ImageView character;
	    private ImageView bread_icon;
	    private ImageView bread;
	    private ImageView knife;
	    private ImageView goldenCroissant;


    // Initialize variables for dimensions in layout
        private int frameHeight;
        private int frameWidth;
        private int screenHeight;
        private int character_width;
        private int character_height;
        private int breadWidth;
        private int breadIconWidth;
        private int goldenCroissantWidth;
        private int knifeWidth;
        private int chairWidth;
        private int chairHeight;
        private int charselect;

    // Positions of sprites
        private int characterX;
        private int characterY;
        private int bread_iconX;
        private int bread_iconY;
        private int breadX;
        private int breadY;
        private int knifeX;
        private int knifeY;
        private int chairX;
        private int chairY;
        private int goldenCroissantX;
        private int goldenCroissantY;

        private int goldenNumber;
        private int goldenGuess;

    // Intialize classes

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    // Status check

        private boolean start_flag = false;
        private boolean pause_flag = false;
        private boolean left_flag = false;
        private boolean right_flag = false;
        private boolean chair_flag = false;
    	private boolean jump_flag = false;
        private boolean reset_flag = false;
        private boolean bonus_flag = false;
        private boolean control;
        private boolean sound_flag;

    // Counter
        private int score = 0;
        private int currentscore;
        private int healthCounter = 3;

    // Button
        private ImageButton pauseButton;
        private ImageButton menu;


    //looper
        private int loop_number = 0;
        private int jump_loop_number;

    // Difficulty Multiplier
        private float speed_multiplier = 1;

        private OrientationData orientationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Runs first time on activity startup, inflates layout of MainActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences control_data = getSharedPreferences("CONTROL_DATA", Context.MODE_PRIVATE);
        control = control_data.getBoolean("CONTROL_DATA", false);

        SharedPreferences sound_data = getSharedPreferences("SOUND_DATA", Context.MODE_PRIVATE);
        sound_flag = sound_data.getBoolean("SOUND_DATA", false);

        sound = new SoundPlayer(this);
        SharedPreferences character_data = getSharedPreferences("CHAR_DATA", Context.MODE_PRIVATE);
        charselect = character_data.getInt("CHAR_DATA", 1);
        // Gets saved dimensions of sprites from xml file: dimension
        Resources res = getResources();
        breadWidth = (int) (res.getDimension(R.dimen.bread));
        knifeWidth = (int) (res.getDimension(R.dimen.knife));
        breadIconWidth = (int) (res.getDimension(R.dimen.bread_icon));
        goldenCroissantWidth = (int) (res.getDimension(R.dimen.goldenCroissant));


        debug = findViewById(R.id.debug1);

        // Assign View objects
        //Characters initialized

        bread_icon = findViewById(R.id.bread_icon);
        bread = findViewById(R.id.bread);
        knife = findViewById(R.id.knife);
        chair = findViewById(R.id.chair);
        goldenCroissant = findViewById(R.id.goldenCroissant);

        // UI initialized
        scoreboard = findViewById(R.id.scoreboard);
        start = findViewById(R.id.start);
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);
        character = findViewById(R.id.character);
        countdown = findViewById(R.id.countdown);
        pauseButton = findViewById(R.id.pause);
        menu = findViewById(R.id.menu);

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        jump = findViewById(R.id.jump);

        // On startup, pause button is not clickable and set countdown to tick from 3
        pauseButton.setClickable(false);
        menu.setClickable(false);
        countdown.setText(Integer.toString(3));

        if (control) {
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        }
        else {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);

            switch (charselect) {
                case 1:
                    character.setImageDrawable(getResources().getDrawable(R.drawable.carbib));
                    break;
                case 2:
                    character.setImageDrawable(getResources().getDrawable(R.drawable.postmaloaf));
                    break;
                case 3:
                    character.setImageDrawable(getResources().getDrawable(R.drawable.yungyeasty));
                    break;
                case 4:
                    character.setImageDrawable(getResources().getDrawable(R.drawable.lilwheaty));
                    break;
            }
        }

            // TODO: offload constants into its own class file file
            // TODO: compatibility for differnt devices
            // Get height of screen for compatibility reasons
            screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            orientationData = new OrientationData();
            orientationData.register();


            // Set objects below screen when initialized
            breadY = screenHeight + 40;
            knifeY = screenHeight + 40;
            bread_iconY = screenHeight + 40;
            chairY = screenHeight + 40;
            goldenCroissantY = screenHeight + 40;

            // Set positions for falling sprites
            bread_icon.setX(-40);
            bread_icon.setY(bread_iconY);
            knife.setX(-40);
            knife.setY(knifeY);
            bread.setX(-40);
            bread.setY(breadY);

            goldenCroissant.setX(-40);
            goldenCroissant.setY(goldenCroissantY);

            chair.setX(-40);
            chair.setY(chairY);

            // Set scoreboard & live counter
            scoreboard.setText("Score: 0");


            orientationData.newGame();

            goldenNumber = (int) Math.floor(Math.random() * 300 + 1);

        }

    // Input X coordinate of falling sprites and a string key, ie "bread", to return if bread is in the x range of other falling sprites
    public boolean avoidStack(String key, int knifeX, int iconx, int breadx) {
        boolean result = false;
        switch (key) {
            case "knife":
                result = (breadx >= knifeX && breadx <= knifeX + breadWidth) || (breadx + knifeWidth >= knifeX && breadx + knifeWidth <= knifeX + breadWidth) && (breadx >= iconx && breadx <= iconx + breadIconWidth) || (breadx + knifeWidth >= iconx && breadx + knifeWidth <= iconx + breadIconWidth);
                break;
            case "bread_icon":
                result = (iconx >= breadx && iconx <= breadx + knifeWidth) || (iconx + breadIconWidth >= breadx && iconx + breadIconWidth <= breadx + knifeWidth) &&  (iconx >= knifeX && iconx <= knifeX + breadWidth) || (iconx + breadIconWidth >= knifeX && iconx + breadIconWidth <= knifeX + breadWidth);
                break;
            case "bread":
                result = (knifeX >= iconx && knifeX <= iconx + breadIconWidth) || (knifeX + breadWidth >= iconx && knifeX + breadWidth <= iconx + breadIconWidth) &&  (knifeX >= iconx && knifeX <= iconx + breadIconWidth) || (knifeX + breadWidth >= iconx && knifeX + breadWidth <= iconx + breadIconWidth);
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

        // Move obstacle bread
        if (breadY > frameHeight) // Move bread above screen once bread falls below screen
            breadY = -200;
        else if (breadY == -200) { // While still above screen, at -100, do initial shuffle and make this statement false. Required since we need to shuffle at least once
            breadX = shufflePos(breadWidth);
            breadY = -199;
        }
        else if (breadY < -breadWidth && avoidStack("bread", breadX, (int)bread_icon.getX(), (int)knife.getX()))  // Shuffle again if bread will collide other objects
            breadX = shufflePos(breadWidth);
        else
            breadY += (12 * speed_multiplier); // Otherwise start falling

        // Set bread coordinates
        bread.setX(breadX);
        bread.setY(breadY);

        // Move obstacle bread_icon

        if (bread_iconY > frameHeight) // Move bread_icon above screen once bread_icon falls below screen
            bread_iconY = -200;
        else if (bread_iconY == -200) { // While still above screen, at -100, do initial shuffle and make this statement false
            bread_iconX = shufflePos(breadIconWidth);
            bread_iconY = -199;
        }
        else if (bread_iconY < -breadIconWidth && avoidStack("bread_icon", (int)bread.getX(), bread_iconX, (int)knife.getX()))  // Shuffle again if bread_icon will collide other objects
            bread_iconX = shufflePos(breadIconWidth);
        else
            bread_iconY += (14 * speed_multiplier); // Otherwise start falling

        bread_icon.setX(bread_iconX);
        bread_icon.setY(bread_iconY);

        // Move obstacle bread

        if (knifeY > frameHeight) // Move knife above screen once knife falls below screen
            knifeY = -200;
        else if (knifeY == -200) { // While still above screen, at -100, do initial shuffle and make this statement false
            knifeX = shufflePos(knifeWidth);
            knifeY = -199;
        }
        else if (knifeY < -knifeWidth && avoidStack("knife", (int)bread.getX(), (int)bread_icon.getX(), knifeX))  // Shuffle again if knife will collide other objects
            knifeX = shufflePos(knifeWidth);
        else
            knifeY += (12 * speed_multiplier); // Otherwise start falling
        
        knife.setX(knifeX);
        knife.setY(knifeY);



        if(goldenGuess != goldenNumber)
            goldenGuess = (int) Math.floor(Math.random() * 300 + 1);
        else if (goldenGuess == goldenNumber && goldenCroissantY < -goldenCroissantWidth)
            goldenCroissantY = -goldenCroissantWidth; // Otherwise start falling
        else if ( goldenCroissantY >= -goldenCroissantWidth) {

            goldenCroissantY += 10;

            if (goldenCroissantY > frameHeight) { // Move knife above screen once knife falls below screen
                goldenCroissantY = -200;
                goldenGuess = 0;
            }
            if (goldenCroissantY == -200) { // While still above screen, at -100, do initial shuffle and make this statement false
                goldenCroissantX = shufflePos(goldenCroissantWidth);
                goldenCroissantY = -199;
                goldenGuess = 0;
            }
        }
        else {
            if (goldenCroissantY > frameHeight)  // Move knife above screen once knife falls below screen
                goldenCroissantY = -200;
        }

        goldenCroissant.setX(goldenCroissantX);
        goldenCroissant.setY(goldenCroissantY);



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

        if (control) {
            if (orientationData.getOrientation() != null && orientationData.getOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 20 * roll / 1f;

                characterX += (int)xSpeed;
                //boundary check and reversal if within bounds
                if ((characterX >= chairX && characterX <= chairX + chairWidth) || (characterX + character_width >= chairX && characterX + character_width <= chairX + chairWidth)) {
                    if (character.getY() + character_height > chair.getY()) {
                        if (characterX + character_width > chairX && characterX > chairX) // Right side collision
                            characterX = chairX + chairWidth + 1;
                        else if (characterX + character_width < chairX + chairWidth && characterX < chairX + chairWidth)
                            characterX = chairX - chairWidth - 1;
                    }
                }
                character.setX((float) characterX);
            }
        }
        
        // Make sure character stays inside the boundry of the screen
        if(!control) {
            if (left_flag)
                characterX -= 20;
            else if (right_flag)
                characterX += 20;

            if ((characterX >= chairX && characterX <= chairX + chairWidth) || (characterX + character_width >= chairX && characterX + character_width <= chairX + chairWidth)) {
                if (character.getY() + character_height > chair.getY()) {
                    if (characterX + character_width > chairX && characterX > chairX)
                        characterX += 20;
                    else if (characterX + character_width < chairX + chairWidth && characterX < chairX + chairWidth)
                        characterX -= 20;
                }
            }
        }

        if (characterX < 0)
            characterX = 0;
        else if (characterX > (frameWidth - character_width))
            characterX = frameWidth - character_width;


        // TODO: add jump mechanic and change characterx
        // Sets the position of the character

//Make sure character stays inside the boundry of the screen in the vertical direction

        if (jump_flag && !reset_flag) {
            reset_flag = true;
            jump_loop_number = loop_number;
        }
        if (reset_flag) {
            if (loop_number - jump_loop_number < 30) {
                characterY = frameHeight - chairHeight - 10 - character_height;
                if (loop_number == jump_loop_number)
                    sound.playJumpSound();
            } else {
                jump_flag = false;
                reset_flag = false;
                characterY = frameHeight - character_height;
            }
        }


        //if(characterY + character_height < frameHeight - chairHeight - 20)
        //else if(characterY > (frameHeight-character_height))

        character.setX(characterX);
        character.setY(characterY);
        // Updates scoreboard
        scoreboard.setText("Score: " + score);
    }

    public void hitCheck() {
        // Boolean statement is basically checking if each corner of an object is inside the character's sprite boundry and plays sound

        // Collision check for bread, add points
        if ((breadX + bread.getWidth() >= characterX) && (breadX <= characterX + character_width) && (breadY + bread.getHeight() >= characterY) && (breadY + bread.getHeight() <= frameHeight)) {
            score += 30;
            breadY = -200;
            sound.playPointSound();
        }

        // Collision check for knife, lose life
        if ((knifeX + knife.getWidth() >= characterX) && (knifeX <= characterX + character_width) && (knifeY + knife.getHeight() >= characterY) && (knifeY + knife.getHeight() <= frameHeight)) {
            knifeY = -200;

            // Reduce life counter every time player touches knife
            healthCounter -= 1;
            sound.playHitSound();
            if(healthCounter == 2)
                life3.setVisibility(View.GONE);
            else if(healthCounter ==1)
                life2.setVisibility(View.GONE);

            // Once zero, call next activity ResultScreen
            if(healthCounter == 0) {
                life1.setVisibility(View.GONE); // Update Life Counter
                timer.cancel();
                timer = null;

                //ends background music
                if (sound_flag)
                    sound.stopBackgroundMusic();

                //play game over sound
                if (sound_flag)
                    sound.playOverSound();

                // Print results
                Intent intent = new Intent(getApplicationContext(), ResultScreen.class);
                intent.putExtra("SCORE", score); // Sends value of score into ResultScreen
                startActivity(intent);
            }// Update Life Counter

        }

        // Collision check for bread_icon, add points, plays hit sound
        if ((bread_iconX + bread_icon.getWidth() >= characterX) && (bread_iconX <= characterX + character_width) && (bread_iconY + bread_icon.getHeight() >= characterY) && (bread_iconY + bread_icon.getHeight() <= frameHeight)) {
            score += 50;
            bread_iconY = -200;
            sound.playPointSound();
        }

        if ((goldenCroissantX + goldenCroissant.getWidth() >= characterX) && (goldenCroissantX <= characterX + character_width) && (goldenCroissantY + goldenCroissant.getHeight() >= characterY) && (goldenCroissantY + goldenCroissant.getHeight() <= frameHeight)) {
            score += 200;
            goldenCroissantY = -200;
            bonus_flag = false;
            goldenGuess = 0;
            sound.playPointSound();
            goldenCroissant.setY(goldenCroissantY);

        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        int pointerCount = me.getPointerCount();

        if (!start_flag) { // If its the player's first time touching the screen on MainActivity, call this
            start_flag = true; // Set true so this doesn't get called again

            FrameLayout gameframe = findViewById(R.id.gameframe); // Initialize the FrameLayout that holds most of the objects, get dimensions
            frameHeight = gameframe.getHeight();
            frameWidth = gameframe.getWidth();

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
                if (inLeftBoundry(me.getX(), me.getY()))  // If position in Object: left, set left flag true. Will move character left in changePos()
                    left_flag = true;
                if (inRightBoundry(me.getX(), me.getY()))  // Move Right
                    right_flag = true;
                if (inJumpBoundry(me.getX(), me.getY()))  //jump
                    jump_flag = true;
            }
            else if (me.getAction() == MotionEvent.ACTION_UP) { // If finger leaves screen, return flags to false
                left_flag = false;
                right_flag = false;
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

            //pause background music
            if (sound_flag)
                sound.pauseBackgroundMusic();

            // Show PAUSED state
            countdown.setVisibility(View.VISIBLE);
            countdown.setText("PAUSED");

            pauseButton.setAlpha(128);
            menu.setVisibility(View.VISIBLE);
            menu.setClickable(true);
            // TODO: change pauseButton.setImageDrawable();
        }
        else { // Resume game and reset flag state
            pause_flag = false;
            resume();
        }
    }

    public void menuPushed(View view) {
        startActivity(new Intent(getApplicationContext(), StartScreen.class));
        if (sound_flag)
            sound.stopBackgroundMusic();
    }

    // Function that runs the game loop
    public void resume() { // Before the game starts, there will be a timer buffer that counts down before game starts
        countdown.setVisibility(View.VISIBLE);


        // CountDownTimer counts down from 3 seconds doing actions every second
        new CountDownTimer(3300, 1000) {

            public void onTick(long millisUntilFinished) {

                menu.setVisibility(View.GONE);
                menu.setClickable(false);
                // TODO: add setting button and replace alpha
                // Make pause button invisible and disable interaction
                pauseButton.setAlpha(128);
                pauseButton.setClickable(false);
                // Print time as timer counts down, prints "BREADY?" when timer is down to 1
                int display = (int)(Math.floor(millisUntilFinished/1000));
                if (display == 1) {

                    countdown.setText("BREADY?");

                    if (!(display < 1))
                        sound.playStartSound();
                }
                else if (display > 1) {
                    countdown.setText(Integer.toString(display));
                    sound.playCountSound();
                }

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
                                loop_number += 1;
                                if (sound_flag)
                                    sound.playBackgroundMusic();
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
        return((x <= jump.getX() + jump.getWidth()) && (x >= jump.getX()) && (y >= jump.getY()));
    }
    // Updates the player difficulty by score
    public float difficulty(int PlayerScore) {
        return 1 + (float)PlayerScore/2000; // Start at difficulty 1
    }
}

