package com.example.eddie.letsgetthisbread;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class CharacterScreen extends AppCompatActivity {

    // Initialzize Classes
    SharedPreferences character_data;
    Resources res;

    // Initialize View Objects
    private TextView current;
    private ImageView chardisplay;

    // Initialize Variables
    private int char_id;

    // Runs when activity is first called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_screen);

        // Initialize View Objects
        current = findViewById(R.id.current);
        chardisplay = findViewById(R.id.chardisplay);

        // Initialize res to get stored strings for character names in res -> values -> strings.xml
        res = getResources();

        // Retrieve previously stored prefreferences
        character_data = getSharedPreferences("CHAR_DATA", Context.MODE_PRIVATE);
        char_id = character_data.getInt("CHAR_DATA" , 1);

        // Call custom fuction "update()" that display current charater selected
        update(char_id);
    }

    // Send user back to main menu
    protected void backtoStart(View view){
        startActivity(new Intent(getApplicationContext(),StartScreen.class));
    }

    // Button for character 1
    public void cardi(View view){
        SharedPreferences.Editor editor3 = character_data.edit();
        char_id = 1;
        editor3.putInt("CHAR_DATA", char_id);
        editor3.commit();
        update(char_id);

    }

    // Button for character 2
    public void post(View view){
        SharedPreferences.Editor editor3 = character_data.edit();
        char_id = 2;
        editor3.putInt("CHAR_DATA", char_id);
        editor3.commit();
        update(char_id);
    }

    // Button for character 3
    public void yung(View view){
        SharedPreferences.Editor editor3 = character_data.edit();
        char_id = 3;
        editor3.putInt("CHAR_DATA", char_id);
        editor3.commit();
        update(char_id);
    }

    // Button for character 4
    public void lil(View view){
        SharedPreferences.Editor editor3 = character_data.edit();
        char_id = 4;
        editor3.putInt("CHAR_DATA", char_id);
        editor3.commit();
        update(char_id);
    }

    // Takes in input id, correspond to specific character and update display to show selected character
    public void update(int id){
        String name;
        switch(id) {
            case 1 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char1));
                current.setText(name);
                chardisplay.setImageResource(R.drawable.carbib);
                break;
            case 2 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char2));
                current.setText(name);
                chardisplay.setImageResource(R.drawable.postmaloaf);
                break;
            case 3 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char3));
                current.setText(name);
                chardisplay.setImageResource(R.drawable.yungyeasty);
                break;
            case 4 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char4));
                current.setText(name);
                chardisplay.setImageResource(R.drawable.lilwheaty);
                break;
        }
    }

    // Disable back buttony
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

