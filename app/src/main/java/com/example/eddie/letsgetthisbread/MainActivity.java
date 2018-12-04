package com.example.eddie.letsgetthisbread;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.example.eddie.letsgetthisbread.GameConstants;

public class MainActivity extends AppCompatActivity {

    private TextView scoreboard;
    private TextView lives;
    private TextView start;
    private TextView left;
    private TextView right;
    private TextView countdown;
    private ImageView character;
    private ImageView cutlery;
    private ImageView knife;
    private ImageView pigeon;

    private TextView debug1;

    // screen & character sizes
    private int frameHeight;
    private int frameWidth;
    private int screenHeight;
    private int character_width;
    private int character_height;

    // Positions
    private int characterX;
    private int characterY;
    private int cutleryX;
    private int cutleryY;
    private int knifeY;
    private int knifeX;
    private int pigeonX;
    private int pigeonY;

    // Intialize classes
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Status check
    private boolean start_flag = false;
    private boolean pause_flag = false;
    private boolean left_flag = false;
    private boolean right_flag = false;
    private boolean control_flag = false; // 0 for screen 1 for motion

    // Counter
    private int score = 0;
    private int healthCounter = 3;

    // Button
    private ImageButton pauseButton;

    // Difficulty Multiplier
    private float speed_multiplier = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreboard = findViewById(R.id.scoreboard);
        start = findViewById(R.id.start);
        character = findViewById(R.id.character);
        cutlery = findViewById(R.id.cutlery);
        knife = findViewById(R.id.knife);
        pigeon = findViewById(R.id.pigeon);
        lives = findViewById(R.id.lives);
        countdown = findViewById(R.id.countdown);

        debug1 = findViewById(R.id.debug1);

        pauseButton = findViewById(R.id.pause);
        pauseButton.setClickable(false);

        countdown.setText(Integer.toString(3));

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        // TODO: offload constants into its own class file file

        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        knifeY = screenHeight + 40;
        pigeonY = screenHeight + 40;
        cutleryY = screenHeight + 40;

        cutlery.setX(-40);
        cutlery.setY(cutleryY);
        pigeon.setX(-40);
        pigeon.setY(pigeonY);
        knife.setX(-40);
        knife.setY(knifeY);

        scoreboard.setText("Score: 0");
        lives.setText(Integer.toString(healthCounter));
    }

    public void changePos() {
        // Collision box check
        hitCheck();

        speed_multiplier = difficulty(score); // only update after current fall

        // TODO: make it so score has a multiplier for each bread not dropped
        // TODO: add bonus object for achievement system

        // Move obstacle knife
        if (knife.getY() > frameHeight) {
            knifeY = -40;
        }
        else if (knife.getY() <= -40){ // when out of screen, randomize
            knifeX = (int) Math.floor(Math.random() * (frameWidth - knife.getWidth()));
        }
        knifeY += (12 * speed_multiplier);

        debug1.setText(Float.toString(speed_multiplier));

        knife.setX(knifeX);
        knife.setY(knifeY);

        // Move obstacle cutlery
        if (cutlery.getY() > frameHeight) {
            cutleryY = -40;
        }
        else if (cutlery.getY() <= -40){ // when out of screen, randomize
            cutleryX = (int) Math.floor(Math.random() * (frameWidth - cutlery.getWidth()));
        }
        cutleryY += (14 * speed_multiplier);

        cutlery.setX(cutleryX);
        cutlery.setY(cutleryY);

        // Move obstacle pigeon

        if (pigeon.getY() > frameHeight) {
            pigeonY = -40;
        }
        else if (pigeon.getY() <= -40){ // when out of screen, randomize
            pigeonX = (int) Math.floor(Math.random() * (frameWidth - pigeon.getWidth()));
        }
        pigeonY += (18 * speed_multiplier);

        pigeon.setX(pigeonX);
        pigeon.setY(pigeonY);

        // Move main character
        // TODO: change action flag mechanic into button & tilt

        if (left_flag) {
            characterX -= 20;
        }
        else if (right_flag) {
            characterX += 20;
        }

        if (characterX < 0) {
            characterX = 0;
        }

        else if (characterX > (frameWidth - character_width)) {
            characterX = frameWidth - character_width;
        }

        // TODO: add jump mechanic and change characterx
        characterY = frameHeight - character_height;
        character.setX(characterX);
        character.setY(characterY);

        scoreboard.setText("Score: " + score);
    }

    public void hitCheck() {
        // Collision counts as the entire box

        // Collision check for knife
        if ((knifeX + knife.getWidth() >= characterX) && (knifeX <= characterX + character_width) && (knifeY + knife.getHeight() >= characterY) && (knifeY + knife.getHeight() <= frameHeight)) {
            score += 30;
            knifeY = -40;
        }

        // Collision check for pigeon
        if ((pigeonX + pigeon.getWidth() >= characterX) && (pigeonX <= characterX + character_width) && (pigeonY + pigeon.getHeight() >= characterY) && (pigeonY + pigeon.getHeight() <= frameHeight)) {

            pigeonY = -40;
            healthCounter -= 1;

            if(healthCounter == 0) {
                timer.cancel();
                timer = null;

                // Print results
                Intent intent = new Intent(getApplicationContext(), ResultScreen.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
                lives.setText("X");
            }
            else {
                lives.setText(Integer.toString(healthCounter));
            }
        }

        // Collision check for cutlery
        if ((cutleryX + cutlery.getWidth() >= characterX) && (cutleryX <= characterX + character_width) && (cutleryY + cutlery.getHeight() >= characterY) && (cutleryY + cutlery.getHeight() <= frameHeight)) {
            score += 50;
            cutleryY = -40;
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!start_flag) {
            start_flag = true;

            FrameLayout game_frame = findViewById(R.id.game_frame);
            frameHeight = game_frame.getHeight();
            frameWidth = game_frame.getWidth();

            characterX = (int)character.getX();
            characterY = (int)character.getY();

            character_width = character.getWidth();
            character_height = character.getHeight();

            start.setVisibility(View.GONE);
            pauseButton.setClickable(true);

            resume();
        }
        else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                if (inLeftBoundry(me.getX(),me.getY())) {
                    left_flag = true;
                }
                if (inRightBoundry(me.getX(),me.getY())) {
                    right_flag = true;
                }
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                left_flag = false;
                right_flag = false;
            }
        }

        return true;
    }

    // Disable return

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

    public void pausePushed(View view) {
        if (!pause_flag) {
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
        else {
            pause_flag = false;
            resume();
        }
    }

    public void resume() {
        countdown.setVisibility(View.VISIBLE);

        new CountDownTimer(2900, 1000) {
            public void onTick(long millisUntilFinished) {
                // Make pause button invisible and disable interaction TODO: add setting button and replace alpha
                pauseButton.setAlpha(128);
                pauseButton.setClickable(false);

                // Print time
                int display = (int)(1 + Math.ceil(millisUntilFinished/1000));
                if (display < 2)
                    countdown.setText("BREADY?");
                else
                    countdown.setText(Integer.toString(display));

            }

            public void onFinish() {
                countdown.setVisibility(View.INVISIBLE);

                // Make pause button visible and enable interaction
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
                                changePos();
                            }
                        });
                    }
                }, 0, 20);
            }
        }.start();
    }

    // TODO: Make boundry check more general
    public boolean inLeftBoundry(float x, float y) {
        return ((x <= left.getX() + left.getWidth()) && (x >= left.getX()) && (y >= left.getY()) && (y <= left.getY() + left.getHeight()));
    }

    public boolean inRightBoundry(float x, float y) {
        return ((x <= right.getX() + right.getWidth()) && (x >= right.getX()) && (y >= right.getY()) && (y <= right.getY() + right.getHeight()));
    }

    public float difficulty(int PlayerScore) {
        return 1 + (float)PlayerScore/1000; // Start at difficulty 1
    }

    public float random_Pos(int range) {


        return 1f;
    }

    public void update(ImageView object, int multiplier) {


    }

    public void control() {
        Intent i = getIntent();
        if ("m" == i.getStringExtra("control"))
            left.setText("M");
        else
            left.setText("S");
    }
}
