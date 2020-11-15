package com.example.mojagra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PoczatkowaAktywnosc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poczatkowa_aktywnosc);
        // tworzenie wątku
        Thread thread = new Thread(){
            @Override
            public void run() {
                //po 5 sekundach
                try {
                    sleep(5000);

                }catch (Exception e){
                    // łapanie wyjątku
                    e.printStackTrace();
                }
                finally {
                    // przenosimy sie do MainActivity
                    Intent mainIntent = new Intent(PoczatkowaAktywnosc.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };
        // start wątku
        thread.start();
    }

    @Override
    protected void onPause() {
        //po pauzie do mainIntenda
        super.onPause();
        finish();
    }
}