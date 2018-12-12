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

    // Initialize Variables
    private boolean action_flag = false;

    // Initialize view
    private ImageView tutorialPG1;

    // Initialize Buttons on Start Screen
    private Button startButton;
    private Button settingButton;
    private Button tutorialButton;
    private Button characterButton;

    // Runs once on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        // Initialize objects
        tutorialPG1 = findViewById(R.id.tutorial_pg1);
        tutorialButton = findViewById(R.id.tutorialButton);
        startButton = findViewById(R.id.startButton);
        settingButton = findViewById(R.id.settingButton);
        characterButton = findViewById(R.id.characterButton);
    }

    // Send user to game activity
    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    // Send user to setting activty
    public void startSettings(View view) {
        startActivity(new Intent(getApplicationContext(), SettingsScreen.class));
    }

    // Send user to character selection activty
    public void characterScreen(View view){
        startActivity(new Intent(getApplicationContext(), CharacterScreen.class));
    }

    // Make tutorial images visible and make other UI invisible
    public void revealTutorial(View view) {
        tutorialPG1.setVisibility(View.VISIBLE);
        tutorialButton.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);
        settingButton.setVisibility(View.GONE);
        characterButton.setVisibility(View.GONE);

    }

    // Detecting user pressing screen, used to make tutorial invisible again and make UI appear
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            action_flag = true;
            if (tutorialPG1.getVisibility() == View.VISIBLE) {
                tutorialPG1.setVisibility(View.INVISIBLE);
                tutorialButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                settingButton.setVisibility(View.VISIBLE);
                characterButton.setVisibility(View.VISIBLE);
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
