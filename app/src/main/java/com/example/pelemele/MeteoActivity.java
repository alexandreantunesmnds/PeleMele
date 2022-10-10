package com.example.pelemele;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class MeteoActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private final int GPS =686;
    private final String KEY = "a5925a9a3cae31b4b32f3e4edd942bd3";
    private double lat;
    private double longi;
    private String LINK = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longi + "&appid="+KEY+"&units=metric&lang=fr";
    private String temp,vent,description,ville, micon;
    private ImageView mweatherIcon;
    private int mCondition;
    private Button boutonLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mweatherIcon = findViewById(R.id.imageView3);
        boutonLoc = findViewById(R.id.button9);
        boutonLoc.setEnabled(true);
    }

    /**
     * Set action on click of view
     * @param view On click button action
     */
    public void clickGeo(View view){
        if(checkPermission()){
            actionGetUserCoords();
        }
    }

    /**
     * Get longitude and altiude and display it with threads
     */
    @SuppressLint("MissingPermission")
    private void actionGetUserCoords(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                //Toast.makeText(MeteoActivity.this, "Lo: "+location.getLongitude()+" / La: "+location.getLatitude(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MeteoActivity.this,"Affichage de la météo",Toast.LENGTH_SHORT).show();
                lat = location.getLatitude();
                longi = location.getLongitude();
                LINK = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longi + "&appid="+KEY+"&units=metric&lang=fr";
                boutonLoc.setEnabled(false);
                boutonLoc.setBackgroundColor(Color.TRANSPARENT);
                boutonLoc.setTextColor(Color.TRANSPARENT);
                actionDisplayMeteo();
            }else{
                Toast.makeText(MeteoActivity.this, "Position inconnue, aucune connexion réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void actionDisplayMeteo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("LIEN",LINK);
                    InputStream in = new java.net.URL(LINK).openStream();
                    JSONObject res = readStream(in);
                    temp = res.getJSONObject("main").getString("temp");
                    description = res.getJSONArray("weather").getJSONObject(0).getString("description");
                    vent =res.getJSONObject("wind").getString("speed");
                    ville =res.getString("name");
                    mCondition=res.getJSONArray("weather").getJSONObject(0).getInt("id");
                    micon=updateWeatherIcon(mCondition);
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(() -> {
                    float temperat = Float.parseFloat(temp);
                    int temperature = (int) temperat;
                    TextView v = findViewById(R.id.textView);
                    TextView villet = findViewById(R.id.textView3);
                    TextView temper = findViewById(R.id.textView2);
                    int resourceID=getResources().getIdentifier(micon,"drawable",getPackageName());
                    mweatherIcon.setImageResource(resourceID);
                    v.append("\nDescription : " + description + "\nVent : " + vent + " m/s"); //reste à ajouter l'icône
                    villet.append(" "+ville);
                    temper.setText(temperature+"°C");
                });
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
    }






//----------------------------------------------------------------------------------------------------------------//
    /**
     * Check if Permission available
     * @return if permission is available
     */
    private boolean checkPermission(){
        boolean res = true;
        if ((ContextCompat.checkSelfPermission(MeteoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ||  (ContextCompat.checkSelfPermission(MeteoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            //When permissions is not granted,Request permission
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},GPS);
            res=false;
        }
        return res;
    }
    /**
     * This method may start an activity allowing the user to choose which permissions to grant and which to reject.
     * @param requestCode The request code passed in onRequestPermissionsResult
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // If request is cancelled, the result arrays are empty.
            case 686:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MeteoActivity.this, "Localisation autorisée", Toast.LENGTH_SHORT).show();
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(MeteoActivity.this, "Localisation non autorisée", Toast.LENGTH_SHORT).show();
                }break;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }
    //----------------------------------------------------------------------------------------------------------------//
    private JSONObject readStream(InputStream is) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();
        return new JSONObject(sb.toString());
    }

    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<300)
        {
            return "thunderstorm1";
        }
        else if(condition>=300 && condition<500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<600)
        {
            return "shower";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunderstorm1";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstrom2";
        }

        return "weather"; //image météo inconnue


    }

}