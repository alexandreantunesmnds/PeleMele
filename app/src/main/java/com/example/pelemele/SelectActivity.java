package com.example.pelemele;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SelectActivity extends AppCompatActivity {

    private SurfaceView sv;
    private ImageView im;
    private Paint paint;
    private int x = 0;
    private int y = 0;
    private int x2 = 0;
    private int y2 = 0;
    private Rect rect;
    private Canvas canvas;
    private SurfaceHolder holder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


        im = (ImageView) findViewById(R.id.select);
        sv = findViewById( R.id.surfaceView);
        sv.setZOrderOnTop(true);
        holder = sv.getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        paint = new Paint();
        paint.setStrokeWidth(10);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if(rect != null){
                coupeImage();
            }
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 2) {

                x = (int) event.getX(0);
                y = (int) event.getY(0);
                x2 = (int) event.getX(1);
                y2 = (int) event.getY(1);
                rect = new Rect(x, y, x2, y2);
                rect.sort();
                canvas = holder.lockCanvas();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.MAGENTA);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                canvas.drawRect(rect, paint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
            return true;
        }
    public void coupeImage(){
        Dialog couper = new Dialog(this);
        BitmapDrawable bmpDraw = (BitmapDrawable) im.getDrawable();
        Bitmap bmp = bmpDraw.getBitmap();

        Bitmap redimension = Bitmap.createScaledBitmap(bmp,im.getWidth(),im.getHeight(),false);
        if(x>=0 && y>= 0) {
            int x = (int) (rect.left - im.getX());
            int y = (int) (rect.top - im.getX());
            int hauteur = (int) ((((rect.bottom - im.getY())) - (rect.top - im.getY())));
            int largeur = (int) ((((rect.right - im.getX())) - (rect.left - im.getX())));

            if(y+hauteur < redimension.getHeight()) {
                Bitmap rog = Bitmap.createBitmap(redimension, x, y, largeur, hauteur);

                ImageView rogView = new ImageView(this);
                rogView.setImageBitmap(rog);

                couper.setContentView(rogView);
                couper.show();
            }
            else{
                Toast.makeText(this, "Veuillez sélectionner une zone à l'intérieure de l'image", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Veuillez sélectionner une zone à l'intérieure de l'image", Toast.LENGTH_SHORT).show();
        }

    }
}
