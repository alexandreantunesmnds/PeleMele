package com.example.pelemele;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.ContactsContract.CommonDataKinds.Photo.PHOTO;

public class MainActivity extends AppCompatActivity {
    //TODO demander toutes les autorisations sur l'app + revoir la météo!!
    //TODO Revoir interface
    private final int PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(v -> Toast.makeText(MainActivity.this, com.example.pelemele.R.string.Bonjour, Toast.LENGTH_SHORT).show());
        Log.i("MainActivity", getString(R.string.info));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.suspendre:
                this.finish();
                return true;
            case R.id.lancer:
                Intent intent = new Intent(this, ChronometreActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void clickChrono(View view) {
        Intent intent = new Intent(this, ChronometreActivity.class) ;
        startActivity(intent) ;
    }

    public void clickPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE) ;
        if (intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Toast.makeText(MainActivity.this, "Hauteur de l'image prise : "+ imageBitmap.getHeight(), Toast.LENGTH_SHORT).show();
            FileOutputStream fos;
            try {
                fos = openFileOutput("image.data", MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            try {
                fos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void LongActivity(View view) {
        Intent intent = new Intent(this, LongActivity.class) ;
        startActivity(intent) ;
    }

    public void clickOpen(View view) {
        Intent intent = new Intent(this, OpenActivity.class) ;
        startActivity(intent) ;
    }

    public void clickMeteo(View view) {
        Intent intent = new Intent(this, MeteoActivity.class) ;
        startActivity(intent) ;
    }

    public void ContactActivity(View view) {
        Intent intent = new Intent(this, ContactActivity.class) ;
        startActivity(intent) ;

    }

    public void clickAccelero(View view) {
        Intent intent = new Intent(this, CapteursActivity.class) ;
        startActivity(intent) ;
    }

    public void clickSelect(View view) {
        Intent intent = new Intent(this, SelectActivity.class) ;
        startActivity(intent) ;
    }


    /*
    public void clickBonjour(View view) {
        Toast.makeText(MainActivity.this,"Bonjour", Toast.LENGTH_SHORT).show() ;
    }*/
}