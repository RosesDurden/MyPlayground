package com.example.roses.myplayground;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatTextView;

import java.util.ArrayList;
import com.facebook.Session;


public class MainActivity extends ActionBarActivity implements LocationListener {

    Boolean gps;
    Button trouverTerrain;
    TextView textArea;
    LocationManager lm;
    Intent cord, connexion;
    ActivityCoordonnees a = new ActivityCoordonnees(this);
    WifiManager wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
        }
        catch(Exception provider){
            Log.d("Error : ", provider.toString());
        }

        cord = new Intent(this, MaLocation.class);
        connexion = new Intent(this, Connexion.class);
        trouverTerrain = (Button)findViewById(R.id.buttonTrouverTerrain);
        textArea = (TextView)findViewById(R.id.TextArea);

        trouverTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getApplicationContext(), "L'application ne fonctionnera pas sans l'activation du GPS.",
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Votre GPS n'est pas activé. Souhaitez-vous l'activer?").setPositiveButton("Oui", dialogClickListener)
                            .setNegativeButton("Non", dialogClickListener).show();
                }
                /**if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getApplicationContext(), "L'application ne fonctionnera pas sans l'activation du réseau de données.",
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Votré réseau de données n'est pas activé. Souhaitez-vous l'activer?").setPositiveButton("Oui", dialogClickListener)
                            .setNegativeButton("Non", dialogClickListener).show();
                }**/
                else {
                    startActivity(cord);
                    //startActivity(connexion);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        gps = true;
        if(LocationManager.GPS_PROVIDER.equals(provider)){
            Toast.makeText(this,"GPS actif",Toast.LENGTH_SHORT).show();
        }
        if(LocationManager.NETWORK_PROVIDER.equals(provider)){
            Toast.makeText(this,"Réseau de données actif",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        gps=false;
        if(LocationManager.GPS_PROVIDER.equals(provider)){
            Toast.makeText(this,"GPS désactivé",Toast.LENGTH_SHORT).show();
        }
        if(LocationManager.NETWORK_PROVIDER.equals(provider)){
            Toast.makeText(this,"Réseau de données désactivé",Toast.LENGTH_SHORT).show();
        }
    }
}
