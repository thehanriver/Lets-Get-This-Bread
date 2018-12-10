package com.example.eddie.letsgetthisbread;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.content.Intent;
import android.view.View;

public class SettingsScreen extends AppCompatActivity {

    SharedPreferences control_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        control_data = getSharedPreferences("GAME_DATA" , Context.MODE_PRIVATE);
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
        SharedPreferences.Editor editor = control_data.edit();
        editor.putBoolean("GAME_DATA" , true);
        editor.commit();

    }

    public void button(View view) {
        SharedPreferences.Editor editor = control_data.edit();
        editor.putBoolean("GAME_DATA" , false);
        editor.commit();
    }
    public void mainMenu(View view){
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
        }
}
