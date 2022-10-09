package com.example.pelemele;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class CapteursActivity extends AppCompatActivity {
    private ImageView north;
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private boolean activation;

    private float [] lastAccelerometer = new float [3];
    private float[] lastMagnetometer = new float [3];

    private DessinVecteur dessin;
    private ExecutorService executorService;

    private float[] floatOrientation = new float [3];
    private float[] floatRotationMatrix = new float [9];
    protected float xA, yA, zA,xM,yM,zM;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mSensorEventListener, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(mSensorEventListener,mAccelerometer);
        sensorManager.unregisterListener(mSensorEventListener,mMagnetometer);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capteurs);
        north = findViewById(R.id.northPoint);
        this.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        executorService = Executors.newFixedThreadPool(4);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        dessin = new DessinVecteur(this);
        activation = false;
    }

    public void clickCapteur(View view) {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchCapt = findViewById(R.id.switchCapt);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(!activation){
            activation = true;
            Toast.makeText(this, "Capteur activé !", Toast.LENGTH_SHORT).show();
            onResume();
            switchCapt.setText("Capteur Activé");
        }
        else {
            Toast.makeText(this, "Capteur desactivé !", Toast.LENGTH_SHORT).show();
            onPause();
            switchCapt.setText("Capteur Désactivé");
            activation = false;

        }
    }
    /**
     * Listener that handles sensor events
     */
    private final SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values,0,lastAccelerometer,0,event.values.length);
                xA = event.values[0];
                yA = event.values[1];
                zA = event.values[2];
                //draw update vecteur
                executorService.execute(new Runnable(){
                    @Override
                    public void run() {
                        dessin.rebuild(event.values);
                    }
                });
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values,0,lastMagnetometer,0,event.values.length);
                xM = event.values[0];
                yM = event.values[1];
                zM = event.values[2];
            }
            SensorManager.getRotationMatrix(floatRotationMatrix,null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(floatRotationMatrix,floatOrientation);
            north.setRotation(floatOrientation[0]* 360 / (2 * 3.14159f));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

}