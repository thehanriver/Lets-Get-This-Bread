package com.example.eddie.letsgetthisbread;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreboard;
    private TextView start;
    private ImageView bread;
    private ImageView cutlery;
    private ImageView knife;
    private ImageView pigeon;

    // screen & character sizes
    private int screenWidth;
    private int frameHeight;
    private int bread_width;
    private int bread_height;

    // Positions
    private int breadX;
    private int breadY;
    private int cutleryX;
    private int cutleryY;
    private int knifeX;
    private int knifeY;
    private int pigeonX;
    private int pigeonY;
    
    // Intialize classes
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Status check
    private boolean action_flag = false;
    private boolean start_flag = false;
    private boolean pause_flag = false;

    // Scoreboard
    private int score = 0;

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

        pauseButton = findViewById(R.id.pause);

        // Getting screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        frameHeight = size.y;

        cutlery.setX(-100);
        cutlery.setY(-100);
        pigeon.setX(-100);
        pigeon.setY(-100);
        knife.setX(-100);
        knife.setY(-100);

        scoreboard.setText("Score: 0");
    }

    public void changePos() {
        // Collision box check
        hitCheck();

        // Move obstacle knife
        knifeY += 16; // TODO: make velocity change with time
        if (knifeY < 0) {
            knifeY = frameHeight - 20; // move obj out of screen
            knifeX = (int) Math.floor(Math.random() * (screenWidth - knife.getWidth()));
        }
        knife.setX(knifeX);
        knife.setY(knifeY);

        // Move obstacle cutlery
        cutleryY += 16; // TODO: make velocity change with time
        if (cutleryY < 0) {
            cutleryY = frameHeight - 20; // move obj out of screen
            cutleryX = (int) Math.floor(Math.random() * (screenWidth - cutlery.getWidth()));
        }
        cutlery.setX(cutleryX);
        cutlery.setY(cutleryY);

        // Move obstacle pigeon
        pigeonY += 16; // TODO: make velocity change with time
        if (pigeonY < 0) {
            pigeonY = frameHeight - 20; // move obj out of screen
            pigeonX = (int) Math.floor(Math.random() * (screenWidth - pigeon.getWidth()));
        }
        pigeon.setX(pigeonX);
        pigeon.setY(pigeonY);

        // Move main character
        // TODO: change action flag mechanic into button & tilt
        if (action_flag) {
            breadX -= 20;
        }
        else {
            breadX += 20;
        }

        if (breadX < 0) {
            breadX = 0;
        }
        else if (breadX > (screenWidth - bread_width)) {
            breadX = (screenWidth - bread_width);
        }

        // TODO: add jump mechanic and change breadx

        bread.setX(breadX);
        breadY = frameHeight - bread.getHeight();
        bread.setY(breadY);

        scoreboard.setText("Score: " + score);
    }

    public void hitCheck() {
        // Collision counts as the entire box

        //TODO: scale down the collision box to make it easier & realistic ie a circular collision

        // Collision check for cutlery
        if ((0 <= cutleryX) && (bread_width + breadX >= cutleryX) && (breadY <= cutleryY + cutlery.getHeight()) && (breadY + bread_height >= cutleryY)) {
            score += 30;
            cutleryY = frameHeight + 20;;
        }
        
        // Collision check for pigeon
        if ((0 <= pigeonX) && (bread_width + breadX >= pigeonX) && (breadY <= pigeonY + pigeon.getHeight()) && (breadY + bread_height >= pigeonY)) {
            pigeonY = frameHeight + 20;

            // Game ends TODO: its optional but we can add multiple lives
            timer.cancel();
            timer = null;

            // Print results
            Intent intent = new Intent(getApplicationContext(), ResultScreen.class);
            intent.putExtra("SCORE" , score);
            startActivity(intent);
        }
        
        // Collision check for knife
        if ((0 <= knifeY) && (bread_width + breadX >= knifeX) && (breadY <= knifeY + knife.getHeight()) && (breadY + bread_height >= knifeY)) {
            score += 50;
            knifeY = frameHeight + 20;;
        }

    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!start_flag) {
            start_flag = true;

            FrameLayout game_frame = findViewById(R.id.game_frame);
            frameHeight = game_frame.getHeight();

            breadX = (int)bread.getX();
            breadY = (int)bread.getY();

            bread_width = bread.getWidth();
            bread_height = bread.getHeight();

            start.setVisibility(View.GONE);

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
                action_flag = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
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

            timer.cancel();
            timer = null;

            // TODO: change pauseButton.setImageDrawable();

        }
        else {
            pause_flag = false;

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
    }
}
