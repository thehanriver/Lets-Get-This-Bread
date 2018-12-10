package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class SettingsScreen extends AppCompatActivity {

    TextView current;
    TextView currentS;
    SharedPreferences control_data;
    SharedPreferences sound_data;

    private boolean control_state;
    private boolean sound_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        // android:text="Control: "

        current = findViewById(R.id.current);
        currentS = findViewById(R.id.currentS);

        control_data = getSharedPreferences("GAME_DATA" , Context.MODE_PRIVATE);
        sound_data = getSharedPreferences("GAME_DATA" , Context.MODE_PRIVATE);
        control_state = control_data.getBoolean("GAME_DATA" , false);
        sound_state = sound_data.getBoolean("GAME_DATA" , false);

        changeText("S", sound_state);
        changeText("M", sound_state);
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

    public void motion(View view) {
        control_state = true;
        SharedPreferences.Editor editor = control_data.edit();
        editor.putBoolean("GAME_DATA" , control_state);
        editor.commit();
        changeText("M", control_state);
    }

    public void button(View view) {
        control_state = false;
        SharedPreferences.Editor editor = control_data.edit();
        editor.putBoolean("GAME_DATA" , control_state);
        editor.commit();
        changeText("M", control_state);
    }

    public void on(View view) {
        sound_state = true;
        SharedPreferences.Editor editor = sound_data.edit();
        editor.putBoolean("GAME_DATA" , sound_state);
        editor.commit();
        changeText("S", sound_state);
    }

    public void off(View view) {
        sound_state = false;
        SharedPreferences.Editor editor = sound_data.edit();
        editor.putBoolean("GAME_DATA" , sound_state);
        editor.commit();
        changeText("S", sound_state);
    }

    public void mainMenu(View view){
        startActivity(new Intent(getApplicationContext(), StartScreen.class));
    }

    public void changeText(String key, boolean bool){
        if (key == "S") {
            if (sound_state)
                currentS.setText("Sound Enabled");
            else
                currentS.setText("Sound Disabled");
        }
        else if (key == "M") {
            if (control_state)
                current.setText("Current Control: Motion");
            else
                current.setText("Current Control: Button");
        }
    }
}