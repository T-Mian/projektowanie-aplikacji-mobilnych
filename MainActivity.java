package com.example.mojagra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // Tworze gre plus gracza
    private Gracz gracz;
    // handler dla obsługi rysowania
    private Handler handler= new Handler();
    private final static long Interval =30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // nowa gra
        gracz = new Gracz(this);
        // zmiana kontentu
        setContentView(gracz);
        // timer dla petli głównej gry
        Timer timer =new  Timer();
        // główna pętla gry (co 30 jednostek czasu odswierzanie handlera)
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gracz.invalidate();
                    }
                });
            }
        }, 0 , Interval);
    }
}