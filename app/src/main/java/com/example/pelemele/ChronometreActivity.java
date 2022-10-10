package com.example.pelemele;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.os.SystemClock.sleep;

public class ChronometreActivity extends AppCompatActivity {
    private long tempsDepart=0;
    private long tempsFin=0;
    private long pauseDepart=0;
    private long pauseFin=0;
    private long duree=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometre);
        Button b = (Button) findViewById(R.id.button5);
        b.setEnabled(false);
    }

    public void clickStop(View view) {
        this.stop(); //le chronomètre s'arrête
        Button b = (Button) findViewById(R.id.button5);
        b.setEnabled(false);
        Button b2 = (Button) findViewById(R.id.button6);
        b2.setEnabled(true);
        String timer = this.getDureeTxt();
        Toast.makeText(ChronometreActivity.this, timer, Toast.LENGTH_SHORT).show(); //affichage du temps passé
        this.resume(); //on réinitialise le chronomètre
    }

    public void clickStart(View view) {
        this.resume(); //on réinitialise le chronomètre
        this.start(); //le chronomètre commence
        Button b = (Button) findViewById(R.id.button6);
        b.setEnabled(false);
        Button b2 = (Button) findViewById(R.id.button5);
        b2.setEnabled(true);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String dateformatted = dateFormat.format(date);
        Toast.makeText(ChronometreActivity.this, dateformatted, Toast.LENGTH_SHORT).show();
    }

    //FONCTIONS SERVANT AU CHRONOMETRE
    public void start()
    {
        tempsDepart=System.currentTimeMillis();
        tempsFin=0;
        pauseDepart=0;
        pauseFin=0;
        duree=0;
    }


    public void resume()
    {
        if(tempsDepart==0) {return;}
        if(pauseDepart==0) {return;}
        pauseFin=System.currentTimeMillis();
        tempsDepart=tempsDepart+pauseFin-pauseDepart;
        tempsFin=0;
        pauseDepart=0;
        pauseFin=0;
        duree=0;
    }

    public void stop()
    {
        if(tempsDepart==0) {return;}
        tempsFin=System.currentTimeMillis();
        duree=(tempsFin-tempsDepart) - (pauseFin-pauseDepart);
        tempsDepart=0;
        tempsFin=0;
        pauseDepart=0;
        pauseFin=0;
    }

    public long getDureeSec()
    {
        return duree/1000;
    }

    public String getDureeTxt()
    {
        return timeToHMS(getDureeSec());
    }

    public static String timeToHMS(long tempsS) {

        // IN : (long) temps en secondes
        // OUT : (String) temps au format texte : "1 h 26 min 3 s"

        int h = (int) (tempsS / 3600);
        int m = (int) ((tempsS % 3600) / 60);
        int s = (int) (tempsS % 60);

        String r="";

        if(h>0) {r+=h+" h ";}
        if(m>0) {r+=m+" min ";}
        if(s>0) {r+=s+" s";}
        if(h<=0 && m<=0 && s<=0) {r="0 s";}

        return r;
    }


}