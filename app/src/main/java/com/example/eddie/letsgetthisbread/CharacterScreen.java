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
import android.widget.TextView;


public class CharacterScreen extends AppCompatActivity {

    SharedPreferences character_data;
    private int char_id;

    private TextView current;
    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_screen);

        res = getResources();
        current = findViewById(R.id.current);

        character_data = getSharedPreferences("CHAR_DATA", Context.MODE_PRIVATE);
        char_id = character_data.getInt("CHAR_DATA" , 1);
        update(char_id);
    }

    protected void backtoStart(View view){
        startActivity(new Intent(getApplicationContext(),StartScreen.class));
    }

    public void cardi(View view){
        SharedPreferences.Editor editor3 = character_data.edit();
        char_id = 1;
        editor3.putInt("CHAR_DATA", char_id);
        editor3.commit();
        update(char_id);

    }

    public void lil(View view){
        SharedPreferences.Editor editor3 = character_data.edit();
        char_id = 4;
        editor3.putInt("CHAR_DATA", char_id);
        editor3.commit();
        update(char_id);
    }

     public void post(View view){
         SharedPreferences.Editor editor3 = character_data.edit();
         char_id = 2;
         editor3.putInt("CHAR_DATA", char_id);
         editor3.commit();
         update(char_id);
     }

     public void yung(View view){
         SharedPreferences.Editor editor3 = character_data.edit();
         char_id = 3;
         editor3.putInt("CHAR_DATA", char_id);
         editor3.commit();
         update(char_id);
     }

    public void update(int id){
        String name;
        switch(id) {
            case 1 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char1));
                current.setText(name);
                break;
            case 2 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char2));
                current.setText(name);
                break;
            case 3 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char3));
                current.setText(name);
                break;
            case 4 :
                name = (res.getString(R.string.dispchar)) + (res.getString(R.string.char4));
                current.setText(name);
                break;
        }
    }

    // Disable back button
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

