package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ResultScreen extends AppCompatActivity {

    // Called when activity first starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        // Initialize View Objects
        TextView scoreboard = findViewById(R.id.scoreboard);
        TextView highscore = findViewById(R.id.highscore);

        // Get stored score from game and set display
        int score = getIntent().getIntExtra("SCORE" , 0);
        scoreboard.setText(score + "");

        // Get stored highscore in app and set display
        SharedPreferences settings = getSharedPreferences("GAME_DATA" , Context.MODE_PRIVATE);
        int stored_highS = settings.getInt("HIGHSCORE" , 0);

        // Check if current game session is higher than high score
        if (score > stored_highS) {
            highscore.setText("High Score: " + score);

            // Save new high score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGHSCORE" , score);
            editor.commit();
        }
        else {
            highscore.setText("High Score: " + stored_highS);
        }
    }

    // Retry game, send user back to game activity
    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    // Back to menu, send user to start activity
    public void backStart(View view) {
        startActivity(new Intent(getApplicationContext(), StartScreen.class));
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
