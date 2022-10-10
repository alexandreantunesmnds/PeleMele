package com.example.pelemele;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class DessinVecteur extends View {
    AppCompatActivity context;
    RelativeLayout lContainerLayout;
    float[] p;
    private float direction;

    public DessinVecteur(Context context) {
        super(context);
        this.context = (AppCompatActivity) context;
        //Fake Container
        this.lContainerLayout = new RelativeLayout(context);
        lContainerLayout.addView(this);
        lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ));
        ((AppCompatActivity) context).addContentView(lContainerLayout,new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startX = (getWidth() / 2);
        int startY = (getHeight() / 2);
        if (p != null) {
            @SuppressLint("DrawAllocation") Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeWidth(10);
            myPaint.setColor(Color.BLUE);
            canvas.drawLine(startX, startY, startX - (p[0] * 50), startY - (p[1] * 50), myPaint);
        }
    }
        public void rebuild(float[]p){
            this.p = p;
            this.invalidate();
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

