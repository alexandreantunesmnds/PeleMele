package com.example.pelemele;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setResult(RESULT_OK) ; // ou RESULT_CANCELED
        finish() ;
    }
}