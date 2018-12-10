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

        control_data = getSharedPreferences("CONTROL_DATA" , Context.MODE_PRIVATE);
        sound_data = getSharedPreferences("SOUND_DATA" , Context.MODE_PRIVATE);
        control_state = control_data.getBoolean("CONTROL_DATA" , false);
        sound_state = sound_data.getBoolean("SOUND_DATA" , false);

        update("S", sound_state);
        update("M", control_state);
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
        update("M", control_state);
    }

    public void button(View view) {
        control_state = false;
        update("M", control_state);
    }

    public void on(View view) {
        sound_state = true;
        update("S", sound_state);
    }

    public void off(View view) {
        sound_state = false;
        update("S", sound_state);
    }

    public void mainMenu(View view){
        update("S", sound_state);
        update("M", control_state);
        startActivity(new Intent(getApplicationContext(), StartScreen.class));
    }

    public void update(String key, boolean bool){
        if (key.equals("S")) {
            SharedPreferences.Editor editor = sound_data.edit();
            editor.putBoolean("SOUND_DATA" , bool);
            editor.commit();

            if (bool)
                currentS.setText("Sound Enabled");
            else
                currentS.setText("Sound Disabled");
        }
        else if (key.equals("M")) {
            SharedPreferences.Editor editor = control_data.edit();
            editor.putBoolean("CONTROL_DATA" , bool);
            editor.commit();

            if (bool)
                current.setText("Current Control: Motion");
            else
                current.setText("Current Control: Button");
        }


    }
}