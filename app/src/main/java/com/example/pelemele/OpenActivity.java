package com.example.pelemele;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        ImageView img = findViewById(R.id.imageView2);
        FileInputStream fis;
        try {
            fis = openFileInput("image.data");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        // Reste à mettre bm à mettre sur l’ImageView
        img.setImageBitmap(bm);
    }
}