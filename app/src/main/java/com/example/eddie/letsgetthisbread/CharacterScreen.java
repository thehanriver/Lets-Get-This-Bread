package com.example.eddie.letsgetthisbread;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class CharacterScreen extends AppCompatActivity {

    SharedPreferences character_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_screen);
        character_data=getSharedPreferences("CHAR_DATA", Context.MODE_PRIVATE);

    }

    protected void backtoStart(View view){
        startActivity(new Intent(getApplicationContext(),StartScreen.class));
    }

    public void cardi(View view){
        SharedPreferences.Editor editor3;
        editor3= character_data.edit();
        editor3.putInt("CHAR_DATA",1);
        editor3.commit();

    }

    public void lil(View view){
        SharedPreferences.Editor editor3;
        editor3= character_data.edit();
        editor3.putInt("CHAR_DATA",2);
        editor3.commit();
    }

     public void post(View view){
         SharedPreferences.Editor editor3;
         editor3=character_data.edit();
         editor3.putInt("CHAR_DATA",3);
         editor3.commit();
     }

     public void yung(View view){
         SharedPreferences.Editor editor3;
         editor3=character_data.edit();
         editor3.putInt("CHAR_DATA",4);
         editor3.commit();

     }




}

