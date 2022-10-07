package com.example.pelemele;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class LongActivity extends AppCompatActivity {
    private ProgressBar loadingimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long);
        this.loadingimage = findViewById(R.id.progressBar);
        this.loadingimage.setVisibility(View.INVISIBLE);
    }

    public void clickStart(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(()->{
                    Toast.makeText(LongActivity.this, "Tâche longue terminée", Toast.LENGTH_SHORT).show();
                    loadingimage.setVisibility(View.INVISIBLE);
                });
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        loadingimage.setVisibility(View.VISIBLE);
        service.execute(runnable);
        //loadingimage.setVisibility(View.VISIBLE);
        //loadingimage.setVisibility(View.INVISIBLE);

    }
}