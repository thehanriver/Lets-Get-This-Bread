package com.example.eddie.letsgetthisbread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView scoreboard;
    private TextView gameframe;
    private ImageView bread;
    private ImageView cutlery;
    private ImageView knife;
    private ImageView pigeon;

    // character position
    private int char_posY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreboard = findViewById(R.id.scoreboard);
        gameframe = findViewById(R.id.gameframe);
        bread = findViewById(R.id.bread);
        cutlery = findViewById(R.id.cutlery);
        knife = findViewById(R.id.knife);
        pigeon = findViewById(R.id.pigeon);

        cutlery.setX(-80);
        cutlery.setY(-80);
        pigeon.setX(-80);
        pigeon.setY(-80);
        knife.setX(-80);
        knife.setY(-80);

        gameframe.setVisibility(View.INVISIBLE);
        char_posY = 500;
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            char_posY -= 20;
        }
        bread.setY(char_posY);

        return true;
    }
}
