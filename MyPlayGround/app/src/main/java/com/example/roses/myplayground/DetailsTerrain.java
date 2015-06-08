package com.example.roses.myplayground;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by roses on 18/12/14.
 */
public class DetailsTerrain extends ActionBarActivity{
    int nbJoueurs;
    String adresse, ville, nomDuTerrain;
    double note;

    private FlatButton flatButton;
    private FlatTextView TextNomDuParcTitre, TextNote, TextVille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_terrains);

        MySQLLite db = new MySQLLite(this);

        /** Set Strings **/
        Intent i = getIntent();
        adresse = i.getStringExtra("AdresseKey");
        nomDuTerrain = i.getStringExtra("NomDuTerrainKey");

        //ville = db.getAdresseTerrain(nomDuTerrain);
        /** Get city String **/
        ville = db.getNomVille(nomDuTerrain);

        /** Set Double **/
        note = db.getNote(nomDuTerrain);
        /** Set Integer **/
        //departement = db.getDepartement(nomDuTerrain);

        /** Get TextViews **/
        TextNomDuParcTitre = (FlatTextView) findViewById(R.id.nomDuParcTitre);
        TextNote = (FlatTextView) findViewById(R.id.note);
        TextVille = (FlatTextView) findViewById(R.id.ville);

        TextNomDuParcTitre.setText(nomDuTerrain);
        TextNote.setText("Note : " + note);
        TextVille.setText("Ville  : "+ville);

        /** Get button **/
        flatButton = (FlatButton)findViewById(R.id.buttonItineraire);
        flatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + adresse;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(map)));
            }
        });
    }
}
