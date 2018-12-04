package com.example.eddie.letsgetthisbread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.content.Intent;
import android.view.View;

public class SettingsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
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
        Intent intent = new Intent(SettingsScreen.this, MainActivity.class);
        intent.putExtra("control", "m");
    }

    public void screen(View view) {
        Intent intent = new Intent(SettingsScreen.this, MainActivity.class);
        intent.putExtra("control", "s");
    }

    public void menu(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
