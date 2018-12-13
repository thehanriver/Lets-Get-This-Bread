package lgtb.proj.eddie.letsgetthisbread;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class SettingsScreen extends AppCompatActivity {

    // Intialize View Objects
    TextView current;
    TextView currentS;

    // Initialized SharedPreferences to access stored data
    SharedPreferences control_data;
    SharedPreferences sound_data;

    // Initialize variables
    private boolean control_state;
    private boolean sound_state;

    // Runs only on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        // Intialize Displays, current to display current control mode and currentS for sound mode
        current = findViewById(R.id.current);
        currentS = findViewById(R.id.currentS);

        // Get stored data for sound and motion
        control_data = getSharedPreferences("CONTROL_DATA" , Context.MODE_PRIVATE);
        control_state = control_data.getBoolean("CONTROL_DATA" , false);
        sound_data = getSharedPreferences("SOUND_DATA" , Context.MODE_PRIVATE);
        sound_state = sound_data.getBoolean("SOUND_DATA" , false);

        // Call custom function "update()" that stores current mode into game storage
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

    // Call button function depending on what the user clicks
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

    // Sends user back to start screen activity and store user's setting into game memory
    public void mainMenu(View view){
        update("S", sound_state);
        update("M", control_state);
        startActivity(new Intent(getApplicationContext(), StartScreen.class));
    }

    // Update function, takes in a key and bool to set its corresponding values in the game memory
    public void update(String key, boolean bool){
        if (key.equals("S")) { // Sound control
            SharedPreferences.Editor editor = sound_data.edit();
            editor.putBoolean("SOUND_DATA" , bool);
            editor.commit();

            if (bool) // Change display
                currentS.setText("Sound Enabled");
            else
                currentS.setText("Sound Disabled");
        }
        else if (key.equals("M")) { // Motion control
            SharedPreferences.Editor editor = control_data.edit();
            editor.putBoolean("CONTROL_DATA" , bool);
            editor.commit();

            if (bool) // Change display
                current.setText("Current Control: Motion");
            else
                current.setText("Current Control: Button");
        }
    }
}