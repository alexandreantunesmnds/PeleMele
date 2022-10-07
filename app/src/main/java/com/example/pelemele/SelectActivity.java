package com.example.pelemele;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.*;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SelectActivity extends AppCompatActivity{
    private ImageView imageView;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private DrawRectView rect;
    protected MotionEvent.PointerCoords pc1,pc2;
    protected AppCompatActivity context;
    private ExecutorService executorService;
    private boolean contactis2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newFixedThreadPool(4);
        pc1 = new MotionEvent.PointerCoords();
        pc2 = new MotionEvent.PointerCoords();
        setContentView(R.layout.activity_select);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        surfaceView.setZOrderOnTop(true);

        //manage the image
        this.imageView = (ImageView) this.findViewById(R.id.select);
        rect=new DrawRectView(this);
        context=this;
        //setContentView(rect);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("SelectActivityOnTouch", String.valueOf(event.getPointerCount()));
                if(event.getPointerCount()==2){
                    contactis2 = true;
                    event.getPointerCoords(0,pc1);
                    event.getPointerCoords(1,pc2);
                    Log.i("SelectActivityOnTouch", pc1.x+"/"+pc1.y+" "+pc2.x+"/"+pc2.y);
                    executorService.execute(new Runnable(){
                        @Override
                        public void run() {
                            rect.rebuid();}});
                }
                else {
                    // Create the object of AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);
                    // Set the message show for the Alert time
                    builder.setMessage("Voici votre rectangle ?");
                    // Set Alert Title
                    builder.setTitle("Affichage du rectangle");
                    // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);
                    // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setNegativeButton("Quitter", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // If user click no then dialog box is canceled.
                        dialog.cancel();
                    });
                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();
                    // Show the Alert Dialog box
                    //alertDialog.show();
                }
                return true;
            }
        });
    }
    public class DrawRectView extends View {
        AppCompatActivity context;
        RelativeLayout lContainerLayout;
        public DrawRectView(AppCompatActivity context) {
            super(context);
            this.context = context;
            this.lContainerLayout = new RelativeLayout(context);
            lContainerLayout.addView(this);
            lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ));
            context.addContentView(lContainerLayout,new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT , ViewGroup.LayoutParams.FILL_PARENT ));

        }
        @Override
        public void onDraw(Canvas canvas) {
            if(pc1!=null && pc2!=null && contactis2){
                @SuppressLint("DrawAllocation") Paint myPaint = new Paint();
                myPaint.setColor(Color.MAGENTA);
                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(10);
                canvas.drawRect(pc1.x+500,pc1.y+50,pc2.x+500,pc2.y+50,myPaint);
            }
            else{
                @SuppressLint("DrawAllocation") Paint myPaint = new Paint();
                myPaint.setColor(Color.MAGENTA);
                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(10);
                canvas = surfaceHolder.lockCanvas();
                canvas.drawRect(pc1.x+500,pc1.y+50,pc2.x+500,pc2.y+50,myPaint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        public void rebuid(){
            this.invalidate();
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
