package com.example.eddie.letsgetthisbread;

import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
    private int screenHeight;
    private int frameHeight;
    private int bread_size;

    // positions
    private int breadY;
    private int cutleryX;
    private int cutleryY;
    private int knifeX;
    private int knifeY;
    private int pigeonX;
    private int pigeonY;
    
    // intialize classes
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // status check
    private boolean action_flag = false;
    private boolean start_flag = false;

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

        // Getting screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenWidth = size.y;

        cutlery.setX(-100);
        cutlery.setY(-100);
        pigeon.setX(-100);
        pigeon.setY(-100);
        knife.setX(-100);
        knife.setY(-100);

    }

    public void changePos() {
        // Move obstacle knife
        knifeX -= 16; // TODO: make velocity change with time
        if (knifeX < 0) {
            knifeX = screenWidth + 20; // move obj out of screen
            knifeY = (int) Math.floor(Math.random() * (frameHeight - knife.getHeight()));
        }
        knife.setX(knifeX);
        knife.setY(knifeY);

        // Move obstacle knife
        cutleryX -= 12; // TODO: make velocity change with time
        if (cutleryX < 0) {
            cutleryX = screenWidth + 20; // move obj out of screen
            cutleryY = (int) Math.floor(Math.random() * (frameHeight - knife.getHeight()));
        }
        cutlery.setX(cutleryX);
        cutlery.setY(cutleryY);

        // Move obstacle knife
        pigeonX -= 20; // TODO: make velocity change with time
        if (pigeonX < 0) {
            pigeonX = screenWidth + 20; // move obj out of screen
            pigeonY = (int) Math.floor(Math.random() * (frameHeight - knife.getHeight()));
        }
        pigeon.setX(pigeonX);
        pigeon.setY(pigeonY);

        // Move main character
        // TODO: change action flag mechanic into button & tilt
        if (action_flag) {
            breadY -= 20;
        }
        else {
            breadY += 20;
        }

        if (breadY < 0) {
            breadY = 0;
        }
        else if (breadY > (frameHeight - bread_size)) {
            breadY = frameHeight - bread_size;
        }

        bread.setY(breadY);
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!start_flag) {
            start_flag = true;

            FrameLayout game_frame = findViewById(R.id.game_frame);
            frameHeight = game_frame.getHeight();

            breadY = (int)bread.getY();

            // TODO: make sure our figure is square, if not, add getLength
            bread_size = bread.getHeight();
            //bread_size = bread.getHeight();

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
}
