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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        TextView scoreboard = findViewById(R.id.scoreboard);
        TextView highscore = findViewById(R.id.highscore);

        int score = getIntent().getIntExtra("SCORE" , 0);
        scoreboard.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA" , Context.MODE_PRIVATE);
        int stored_highS = settings.getInt("HIGHSCORE" , 0);

        if (score > stored_highS) {
            highscore.setText("High Score: " + score);

            // Save score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGHSCORE" , score);
            editor.commit();
        }
        else {
            highscore.setText("High Score: " + stored_highS);
        }
    }

    // add main menu button
    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

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
