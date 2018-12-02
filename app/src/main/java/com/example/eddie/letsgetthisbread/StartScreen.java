package com.example.eddie.letsgetthisbread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StartScreen extends AppCompatActivity {

    // flags for quitting tutorial
    private boolean action_flag = false;

    // Tutorial images
    private ImageView tutorialPG1;

    // Buttons

    private Button startButton;
    private Button settingButton;
    private Button tutorialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        tutorialPG1 = findViewById(R.id.tutorial_pg1);
        tutorialButton = findViewById(R.id.tutorialButton);
        startButton = findViewById(R.id.startButton);
        settingButton = findViewById(R.id.settingButton);
    }

    protected void startGame(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    protected void startSettings(View view) {
        startActivity(new Intent(getApplicationContext(), SettingsScreen.class));
    }

    // Make tutorial images visible
    protected void revealTutorial(View view) {
        tutorialPG1.setVisibility(View.VISIBLE);
        tutorialButton.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);
        settingButton.setVisibility(View.GONE);
    }

    // Detection pressing
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            action_flag = true;
            if (tutorialPG1.getVisibility() == View.VISIBLE) {
                tutorialPG1.setVisibility(View.INVISIBLE);
                tutorialButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                settingButton.setVisibility(View.VISIBLE);
            }
        } else if (me.getAction() == MotionEvent.ACTION_UP) {
            action_flag = false;
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
}
