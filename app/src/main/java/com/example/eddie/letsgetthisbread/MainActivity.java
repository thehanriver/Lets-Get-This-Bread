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
    //private int screenHeight;
    private int frameHeight;
    private int frameWidth;
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
        pauseButton.setVisibility(View.GONE);

        // Getting screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenWidth = size.y;

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
    }

    public void changePos() {
        // Collision box check
        hitCheck();

        // Move obstacle knife
        knifeY += 16; // TODO: make velocity change with time
        if (knifeY > frameHeight) {
            knifeY = -20;
            //knifeY = screenWidth + 20; // move obj out of screen
            knifeX = (int) Math.floor(Math.random() * (frameWidth - knife.getWidth()));
        }
        knife.setX(knifeX);
        knife.setY(knifeY);

        // Move obstacle knife
        cutleryY += 12; // TODO: make velocity change with time
        if (cutleryY > frameHeight) {
            cutleryY = -20;
            //cutleryX = screenWidth + 20; // move obj out of screen
            cutleryX = (int) Math.floor(Math.random() * (frameWidth - cutlery.getWidth()));
        }
        cutlery.setX(cutleryX);
        cutlery.setY(cutleryY);

        // Move obstacle knife
        pigeonY += 20; // TODO: make velocity change with time
        if (pigeonY > frameHeight) {
            pigeonY = -20;
            //pigeonX = screenWidth + 20; // move obj out of screen
            pigeonX = (int) Math.floor(Math.random() * (frameWidth - pigeon.getWidth()));
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

        //TODO: scale down the collision box to make it easier & realistic ie a circular collision

        // Collision check for knife
        if ((knifeX + knife.getWidth() >= breadX) && (knifeX <= breadX + bread_width) && (knifeY + knife.getHeight() >= breadY) && (knifeY + knife.getHeight() <= frameHeight)) {
            score += 30;
            knifeY = -40;
        }
        
        // Collision check for pigeon
        if ((pigeonX + pigeon.getWidth() >= breadX) && (pigeonX <= breadX + bread_width) && (pigeonY + pigeon.getHeight() >= breadY) && (pigeonY + pigeon.getHeight() <= frameHeight)) {
            pigeonY = -40;

            // Game ends TODO: its optional but we can add multiple lives
            timer.cancel();
            timer = null;

            // Print results
            Intent intent = new Intent(getApplicationContext(), ResultScreen.class);
            intent.putExtra("SCORE" , score);
            startActivity(intent);
        }
        
        // Collision check for cutlery
        if ((cutleryX + cutlery.getWidth() >= breadX) && (cutleryX <= breadX + bread_width) && (cutleryY + cutlery.getHeight() >= breadY) && (cutleryY + cutlery.getHeight() <= frameHeight)) {
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

            breadX = (int)bread.getX();
            breadY = (int)bread.getY();

            bread_width = bread.getWidth();
            bread_height = bread.getHeight();

            start.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);

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
