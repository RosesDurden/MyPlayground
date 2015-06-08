package com.example.roses.myplayground;

import android.app.LauncherActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by roses on 16/12/14.
 */
public class MaLocation extends ListActivity{

    ActivityCoordonnees a;
    String adresse[], realAdresse, nomParc ;
    double latitude, longitude, min, encoreAutreLat, encoreAutreLong, good;
    Location mine;
    HashMap<String, Location> map = new HashMap<>(); //HashMap pour stocker les noms des lieux et leurs positions
    HashMap<Double, String> dist = new HashMap<>(); //HashMap qui stocke les distances entre moi et les terrains
    Map<Double, String> sortMap; //HashMap qui ordonne la HashMap dist --> on a une map classée du terrain le plus proche au terrain le plus loin
    Set set2;
    Iterator iterator2;
    MySQLLite db = new MySQLLite(this);
    String nomDuTerrain, googleMaps;
    String[] mStrings;
    Object di;
    Intent detailsTerrain;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> listAdress=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_coordonnees);

        mine = new Location("");
        //Location parcCitadelle = new Location("Parc de la Citadelle");

        a = new ActivityCoordonnees(MaLocation.this);


        listView = (ListView)findViewById(android.R.id.list);

        //Récupération du ListView présent dans notre IHM
        //text = (TextView) findViewById(R.id.text);

        //MySQLLite db = new MySQLLite(this);
        //check if GPS enabled
        if (a.canGetLocation()) {

            latitude = a.getLatitude();
            longitude = a.getLongitude();
            mine.setLatitude(latitude);
            mine.setLongitude(longitude);

            Log.d("Latitude : ", String.valueOf(latitude));
            Log.d("Longitude : ", String.valueOf(longitude));

            /** How to set manually a location
            encoreAutreLat = 48.57725440000001;
            encoreAutreLong = 7.779803799999968;
            parcCitadelle.setLatitude(encoreAutreLat);
            parcCitadelle.setLongitude(encoreAutreLong);**/

            // How to add terrains
            /*db.addTerrain(new TerrainDatabase("Parc de la Citadelle", 48.57725440000001, 7.779803799999968, 67, "Strasbourg", "Rue d'Ankara" , 0 ));
             map.put("Parc de la Citadelle", db.getPosition("Parc de la Citadelle"));

             db.addTerrain(new TerrainDatabase("Gymnasium Schoepflin", 48.58600 , 7.74986, 67, "Strasbourg", "6 Rue de la Fonderie",0 ));
             map.put("Gymnasium Schoepflin", db.getPosition("Gymnasium Schoepflin"));

             db.addTerrain(new TerrainDatabase("PlayGround rue Fritz Kieffer", 48.59452, 7.75244, 67, "Strasbourg","6 Rue Fritz Kieffer", 0 ));
             map.put("PlayGround rue Fritz Kieffer", db.getPosition("PlayGround rue Fritz Kieffer"));

             db.addTerrain(new TerrainDatabase("Playground stade Le Dantec", 49.74891, 0.38892, 76, "Fecamp","Chemin de saint jacques", 0 ));
             map.put("Playground stade Le Dantec", db.getPosition("Playground stade Le Dantec"));

             db.addTerrain(new TerrainDatabase("Playground du Parc de la Pépinière", 48.69806, 6.18500, 54, "Nancy","Boulevard du 26e RI", 0 ));
             map.put("Playground du Parc de la Pépinière", db.getPosition("Playground du Parc de la Pépinière"));

             db.addTerrain(new TerrainDatabase("Playground du Parc de Sainte Marie", 48.68058, 6.17057, 54, "Nancy","Avenue Boffrand", 0 ));
             map.put("Playground du Parc de Sainte Marie", db.getPosition("Playground du Parc de Sainte Marie"));*/

            List<TerrainDatabase> l = db.getAllTerrains();

            for (Iterator<TerrainDatabase> i = l.iterator(); i.hasNext(); ) {
                TerrainDatabase item = i.next();
                /** On récupère les noms de tous les terrains, on les stockent dans la HashMap map et
                 * on les passe en paramètre de getPosition() pour
                 * récupérer la position GPS du terrain en question
                 */
                String nom = item.getNom();
                map.put(nom, db.getPosition(nom));

                /** How to delete terrains**/
                //db.deleteTerrain(item);
            }

            /** Je récupère la distance entre moi et tous les terrains référencés en BDD
             * Puis je stocke ça dans la HashMap dist
             **/
            for (Map.Entry<String, Location> entry : map.entrySet()) {
                Location valeur = entry.getValue();
                String nomTerrain = entry.getKey();
                double distance = mine.distanceTo(valeur) / 1000;
                distance = round(distance, 2);
                dist.put(distance, nomTerrain);
            }

            sortMap = new TreeMap<>(dist);
            set2 = sortMap.entrySet();
            iterator2 = set2.iterator();
            //Je récupère la plus petite distance dans la liste dist
            for (Map.Entry<Double, String> entry : dist.entrySet()) {
                try{
                    while(iterator2.hasNext()) {
                        Map.Entry me2 = (Map.Entry) iterator2.next();
                        //good=entry.getKey();
                        //nomDuTerrain = dist.get(good);
                        di = me2.getKey();
                        nomDuTerrain = dist.get(di);
                        /** Je récupère les champs "Adresse" et "Ville" associé à mon terrain **/
                        String adresseTerrain = db.getAdresseTerrain(nomDuTerrain);
                        final String nomDeVille = db.getNomVille(nomDuTerrain);
                        googleMaps = adresseTerrain + " " + nomDeVille;

                        mStrings = new String[]{"Nom du parc : " + nomDuTerrain + "\nAdresse : " + googleMaps + "\nDistance entre moi et le terrain : " + di + " km."};

                        for (String data : mStrings) {
                            listItems.add(data);
                            adresse = data.split("\n|\\:"); //Regex permettant de récupérer juste l'adresse
                            realAdresse = adresse[3]; // On a les adresses une par une
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedFromList =(String) (listView.getItemAtPosition(position));
                                    adresse = selectedFromList.split("\n|\\:");
                                    nomParc = adresse[1];
                                    realAdresse = adresse[3];

                                    detailsTerrain = new Intent(getApplicationContext(), DetailsTerrain.class);
                                    detailsTerrain.putExtra("AdresseKey", realAdresse);
                                    detailsTerrain.putExtra("NomDuTerrainKey", nomParc);
                                    startActivity(detailsTerrain);
                                }
                            });
                        }

                        ArrayAdapter<String> str = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_layout_for_list_view, listItems);
                        listView.setAdapter(str);
                        str.setNotifyOnChange(true);
                    }
                 }
                 catch(Exception e){e.printStackTrace();}
            }
        }
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(View v) {
        listItems.add("Clicked : ");
        adapter.notifyDataSetChanged();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void printMap(Map<Double, String> map) {
        for (Map.Entry<Double, String> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }
}