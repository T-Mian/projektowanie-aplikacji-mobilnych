package com.example.mojagra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class Gracz extends View {
    // wstepne inicjacje
    private Bitmap gracz_p[]= new Bitmap[2];
    private  Bitmap tlo;
    private Paint wynikPaint = new Paint();
    private  Bitmap zycie []= new Bitmap[2];
    private int gracz_pX=10, gracz_pY ,gracz_p_speed;
    private int plutoSzer, plutoWys;
    private boolean dotyk= false;
    private int wyniki , liczba_zycia;
    private  int cosieX,cosieY, cosieSpeed= 10;
    private  Paint cosie_paint = new Paint();
    private int innyX, innyY, inny_speed = 20;
    private  Paint inny_paint = new Paint();
    private int enemX, enemY, enem_speed = 15;
    private  Paint enem_paint = new Paint();

    public Gracz(Context context) {
        super(context);
        // tu kreuje gracza i ustalam wielkosc postaci(bo grafika jest za duza)
        //klatka 1 animacji
        gracz_p[0] = BitmapFactory.decodeResource(getResources(), R.drawable.lotnik_1);
        //zmiana wymiarow
        int wysok = (gracz_p[0].getHeight() / 6);
        int szerok = (gracz_p[0].getWidth() / 6);
        // przeskalowanie postaci  klatki 1
        gracz_p[0] = Bitmap.createScaledBitmap(gracz_p[0], szerok, wysok,false);
        // klatka 2
        gracz_p[1] = BitmapFactory.decodeResource(getResources(), R.drawable.lotnik_2);
        gracz_p[1] = Bitmap.createScaledBitmap(gracz_p[1], szerok, wysok,false);
        // cosie i inny to koła do zbierania dające punkty
        cosie_paint.setColor( Color.YELLOW);
        cosie_paint.setAntiAlias(false);

        inny_paint.setColor( Color.GREEN);
        inny_paint.setAntiAlias(false);
        //enem zabiera zycie
        enem_paint.setColor( Color.RED);
        enem_paint.setAntiAlias(false);
        // tło do gry i przeskalowanie
        tlo = BitmapFactory.decodeResource(getResources(), R.drawable.tlo);
        int tHa = (tlo.getHeight() / 2);
        int tW = (tlo.getWidth() / 2);
        tlo = Bitmap.createScaledBitmap(tlo,tW,tHa,false);
        // Obiekt Paint dla wyniku
        wynikPaint.setColor(Color.CYAN);
        wynikPaint.setTextSize(50);
        wynikPaint.setTypeface(Typeface.DEFAULT_BOLD);
        wynikPaint.setAntiAlias(true);
        // ikony z sercem (Z Zyciem)
        zycie[0] = BitmapFactory.decodeResource(getResources(),R.drawable.lif1);
        zycie[1] = BitmapFactory.decodeResource(getResources(),R.drawable.lif2);
        // wymiar y , wstepnie wynik i liczba zycia
        gracz_pY = 100;
        wyniki = 0;
        liczba_zycia = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // parametry x i y
        plutoWys = getHeight();
        plutoSzer = getWidth();
        // malowanie tła
        canvas.drawBitmap(tlo,0,0,null);
        // parametry min max wielkosci gracza
        int minGracz = gracz_p[0].getHeight();
        int maxGracz = plutoWys - gracz_p[0].getHeight() ;
        // jakie przesunięcie gracza
        gracz_pY = gracz_pY + gracz_p_speed;
        //zapobieganie wylecenia poza obszar
        if(gracz_pY < minGracz){
            gracz_pY = minGracz;
        }
        if(gracz_pY > maxGracz){
            gracz_pY = maxGracz;
        }
        // zmiana szybkości gracza
        gracz_p_speed = gracz_p_speed + 2 ;
        // zmiany klatki przy dotkniecu ekranu
        if (dotyk){
            canvas.drawBitmap(gracz_p[1], gracz_pX, gracz_pY, null);
            dotyk =false;
        }
        else {
            canvas.drawBitmap(gracz_p[0],gracz_pX,gracz_pY,null);
        }
        // przemieszczenie sie cosia
        cosieX = cosieX - cosieSpeed;
        // co sie dzieje przy zderzeniu z cosiem
        if(zaliczoneUderzenie(cosieX,cosieY)){
            // dodajemy do wyniku 10 punktow i cosie znika z wizji
            wyniki = wyniki +10;
            cosieX = - 100;
        }
        // manipulacja do zrandomizowania pozycji dla cosia, i czos pojawia się troche za ekranem
        if(cosieX <0){
            cosieX = plutoSzer + 21;
            cosieY = (int) Math.floor(Math.random() * (maxGracz - minGracz)) - minGracz;
        }
        //ryswane cosia
        canvas.drawCircle(cosieX, cosieY, 20, cosie_paint);

        // sektor dla innego cosia
        //przesowanie innego
        innyX = innyX - inny_speed;
        // co jak trafimy w innego
        if(zaliczoneUderzenie(innyX,innyY)){
            //zmiana wyniku
            wyniki = wyniki + 20 ;
            // przesówanie poza wizje
            innyX = - 100;
        }
        //randomizacja pojawiania sie innego
        if(innyX <0){
            innyX = plutoSzer + 21;
            innyY = (int) Math.floor(Math.random() * (maxGracz - minGracz)) - minGracz;
        }
        // rysowanie innego
        canvas.drawCircle(innyX, innyY, 20, inny_paint);
        // sektor enem czyli wroga jednostka
        // przemieszczenie enem
        enemX = enemX - enem_speed;
        // konsekwencje zderzenia
        if(zaliczoneUderzenie(enemX,enemY)){
            // odjęcie zycia i przesunięcie poza widok
            liczba_zycia--;
            enemX = - 100;
            // warunki konca gry
            if(liczba_zycia == 0){
              // powołanie nowego Intenta
                Intent gameoverIntent = new Intent(getContext(), GameOver.class);
                // przesunięcie na nowy task
                gameoverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // zapisanie wyniku
                gameoverIntent.putExtra("Score" ,wyniki);
                //start nowy kontekst
                getContext().startActivity(gameoverIntent);
            }
        }
        // randomizacja pojawiania sie nowych wrogów
        if(enemX <0){
            enemX = plutoSzer + 21;
            enemY = (int) Math.floor(Math.random() * (maxGracz - minGracz)) - minGracz;
        }
        // rysowanie wroga
        canvas.drawCircle(enemX, enemY, 30, enem_paint);
        // rysowanie wyników w obecnej grze
        canvas.drawText("Wynik : "+ wyniki,20,60, wynikPaint);

        //petla for dla zarządzaniem grafiki  z sercem
        for (int i = 0;i<3;i++ ){
            // gdzie w x-sie ma być
            int x = (int) (300 + zycie[0].getWidth()* 1.5 * i);
            //w y-greku
            int y = 30;
            //jaka grafika ma być
            if(i < liczba_zycia ){
                canvas.drawBitmap(zycie[0], x, y, null);
            }
            else {
                canvas.drawBitmap(zycie[1],x,y, null);
            }
        }

    }
    public  boolean zaliczoneUderzenie(int x, int y){
        //  warunek sprawdzający czy mamy zderzenie / własciwie buduje kolider w kształcie prostokąta wokół obiektów i gracza
        if(gracz_pX<x && x < (gracz_pX + gracz_p[0].getWidth()) && gracz_pY<y && y < (gracz_pY +gracz_p[0].getHeight())){
            return true;
            // i jak "trafiam" kolider zwraca prawda
        }
        return false;
        //a jak nie to false
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // ewent dotykowy
        if(event.getAction()== MotionEvent.ACTION_DOWN){
            // przydatny boolean dotyk
            dotyk = true;
            // zmiana speed gracza
            gracz_p_speed =-22;
        }
        return  true;
    }
}
