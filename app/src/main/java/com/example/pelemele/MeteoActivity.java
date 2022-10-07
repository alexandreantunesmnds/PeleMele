package com.example.pelemele;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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

import static java.lang.Thread.sleep;

public class MeteoActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private double lat;
    private double longi;
    private String temp;
    private String description;
    private String vent;
    private boolean printed_info; //permet de rendre l'affichage des informations météo visible 1 fois
    private boolean isGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);
        if (ContextCompat.checkSelfPermission(MeteoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MeteoActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MeteoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(MeteoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MeteoActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    this.isGranted = true;
                    getLocation();
                }
            } else if (ContextCompat.checkSelfPermission(MeteoActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                this.isGranted = false;
            }
        }
    }
    @SuppressLint("MissingPermission")
    public void getLocation(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                        // Logic to handle location object
                        lat = location.getLatitude();
                        longi = location.getLongitude();
                });
    }

    private JSONObject readStream(InputStream is) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();
        return new JSONObject(sb.toString());
    }

    public void clickGeo(View view) {
        if (isGranted && !printed_info) {
            Toast.makeText(MeteoActivity.this, "Longitude : " + longi + " Latitude : " + lat, Toast.LENGTH_SHORT).show();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longi + "&appid=a5925a9a3cae31b4b32f3e4edd942bd3&units=metric&lang=fr";
                        InputStream in = new java.net.URL(url).openStream();
                        JSONObject res = readStream(in);
                        temp = res.getJSONObject("main").getString("temp");
                        description = res.getJSONArray("weather").getJSONObject(0).getString("description");
                        vent = res.getJSONObject("wind").getString("speed");
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> {
                        TextView v = findViewById(R.id.textView);
                        v.append("\nTempérature : " + temp + "°C" + "\nDescription : " + description + "\nVent : " + vent + " m/s"); //reste à ajouter l'icône
                    });
                }
            };
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(runnable);
            printed_info = true;
        } else if (!isGranted){
            Toast.makeText(MeteoActivity.this, "Vous devez autoriser la localisation", Toast.LENGTH_SHORT).show();
        }
    }
}