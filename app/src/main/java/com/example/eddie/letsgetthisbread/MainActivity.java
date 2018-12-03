package com.example.eddie.letsgetthisbread;

import android.content.Intent;
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

    private TextView scoreboard;
    private TextView lives;
    private TextView start; // TODO: start can function as paused menu as well
    private TextView left;
    private TextView right;
    private TextView countdown;
    private ImageView bread;
    private ImageView cutlery;
    private ImageView knife;
    private ImageView pigeon;

    // screen & character sizes
    private int frameHeight;
    private int frameWidth;
    private int bread_width;
    private int bread_height;

    // Positions
    private int breadX;
    private int breadY;
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
    private boolean action_flag = false;
    private boolean start_flag = false;
    private boolean pause_flag = false;
    private boolean left_flag = false;
    private boolean right_flag = false;

    // Counter
    private int score = 0;
    private int healthCounter = 3;

    // Button
    private ImageButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreboard = findViewById(R.id.scoreboard);
        start = findViewById(R.id.start);
        bread = findViewById(R.id.bread);
        cutlery = findViewById(R.id.cutlery);
        knife = findViewById(R.id.knife);
        pigeon = findViewById(R.id.pigeon);
        lives = findViewById(R.id.lives);
        countdown = findViewById(R.id.countdown);

        pauseButton = findViewById(R.id.pause);
        pauseButton.setClickable(false);

        countdown.setText(Integer.toString(3));

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        // TODO: offload constants into its own class file file
        knifeY = 3000;
        cutleryY = 3000;
        pigeonY = 3000;

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

        // Move obstacle knife
        knifeY += 16; // TODO: make velocity change with time
        if (knifeY > frameHeight) {
            knifeY = -20;
            knifeX = (int) Math.floor(Math.random() * (frameWidth - knife.getWidth()));
        }
        knife.setX(knifeX);
        knife.setY(knifeY);

        // Move obstacle knife
        cutleryY += 12; // TODO: make velocity change with time
        if (cutleryY > frameHeight) {
            cutleryY = -20;
            cutleryX = (int) Math.floor(Math.random() * (frameWidth - cutlery.getWidth()));
        }
        cutlery.setX(cutleryX);
        cutlery.setY(cutleryY);

        // Move obstacle knife
        pigeonY += 20; // TODO: make velocity change with time
        if (pigeonY > frameHeight) {
            pigeonY = -20;
            pigeonX = (int) Math.floor(Math.random() * (frameWidth - pigeon.getWidth()));
        }
        pigeon.setX(pigeonX);
        pigeon.setY(pigeonY);

        // Move main character
        // TODO: change action flag mechanic into button & tilt

        if (action_flag && left_flag) {
            breadX -= 20;
        }
        else if (action_flag && right_flag) {
            breadX += 20;
        }

        if (breadX < 0) {
            breadX = 0;
        }
        else if (breadX > (frameWidth - bread_width)) {
            breadX = frameWidth - bread_width;
        }

        // TODO: add jump mechanic and change breadx
        breadY = frameHeight - bread_height;
        bread.setX(breadX);
        bread.setY(breadY);

        scoreboard.setText("Score: " + score);
    }

    public void hitCheck() {
        // Collision counts as the entire box

        // Collision check for knife
        if ((knifeX + knife.getWidth() >= breadX) && (knifeX <= breadX + bread_width) && (knifeY + knife.getHeight() >= breadY) && (knifeY + knife.getHeight() <= frameHeight)) {
            score += 30;
            knifeY = -40;
        }

        // Collision check for pigeon
        if ((pigeonX + pigeon.getWidth() >= breadX) && (pigeonX <= breadX + bread_width) && (pigeonY + pigeon.getHeight() >= breadY) && (pigeonY + pigeon.getHeight() <= frameHeight)) {

            pigeonY = -40;
            healthCounter -= 1;

            if(healthCounter == 0) {   // Game ends TODO: its optional but we can add multiple lives
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
        if ((cutleryX + cutlery.getWidth() >= breadX) && (cutleryX <= breadX + bread_width) && (cutleryY + cutlery.getHeight() >= breadY) && (cutleryY + cutlery.getHeight() <= frameHeight)) {
            score += 50;
            cutleryY = -40;
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!start_flag) {
            // TODO: add countdown b4 game starts
            start_flag = true;

            FrameLayout game_frame = findViewById(R.id.game_frame);
            frameHeight = game_frame.getHeight();
            frameWidth = game_frame.getWidth();

            breadX = (int)bread.getX();
            breadY = (int)bread.getY();

            bread_width = bread.getWidth();
            bread_height = bread.getHeight();

            start.setVisibility(View.GONE);
            pauseButton.setClickable(true);

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
        else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                if (inLeftBoundry(me.getX(),me.getY())) {
                    action_flag = true;
                    left_flag = true;
                }
                if (inRightBoundry(me.getX(),me.getY())) {
                    action_flag = true;
                    right_flag = true;
                }
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
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
            countdown.setVisibility(View.VISIBLE);

            new CountDownTimer(2900, 1000) {
                public void onTick(long millisUntilFinished) {
                    // Make pause button invisible and disable interaction TODO: add setting button and replace alpha
                    pauseButton.setAlpha(128);
                    pauseButton.setClickable(false);

                    // Print time
                    countdown.setText(Integer.toString((int)(1 + Math.ceil(millisUntilFinished/1000))));
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
    }
    
    public boolean inLeftBoundry(float x, float y) {
        return ((x <= left.getX() + left.getWidth()) && (x >= left.getX()) && (y >= left.getY()) && (y <= left.getY() + left.getHeight()));
    }
    
    public boolean inRightBoundry(float x, float y) {
        return ((x <= right.getX() + right.getWidth()) && (x >= right.getX()) && (y >= right.getY()) && (y <= right.getY() + right.getHeight()));
    }
}
