package com.example.mojagra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    //inicjalizacja wstępna
    private Button PonownaGra;
    private TextView PokarzScore;
    private  String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        // 22 i 23 linijka obsługa wyników wyswietlanych po grze
        PokarzScore = (TextView) findViewById(R.id.pokaz_score);
        score = getIntent().getExtras().get("Score").toString();
        // guzik dla nowej gry
        PonownaGra = (Button) findViewById(R.id.nowa_gra);
        // co zrobi po kliknieciu
        PonownaGra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // powrót do gry
                Intent mainIntent = new Intent(GameOver.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
        // wyswietla wynik gry
        PokarzScore.setText( "wynik to " + score + " punktów");
    }
}