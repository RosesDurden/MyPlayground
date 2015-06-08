package com.example.roses.myplayground;

/**
 * Created by roses on 16/12/14.
 */
public class TerrainDatabase {

    private int id;
    private String nom;
    private double latitude;
    private double longitude;
    private int departement;
    private String ville;
    private String positionMap;
    private double note;


    public TerrainDatabase(){}

    public TerrainDatabase(String iNom, double iLatitude, double iLongitude, int iDepartement, String iVille, String iPositionMap, double iNote ) {
        super();
        this.nom = iNom;
        this.latitude = iLatitude;
        this.longitude = iLongitude;
        this.departement = iDepartement;
        this.ville = iVille;
        this.positionMap = iPositionMap;
        this.note = iNote;
    }

    @Override
    public String toString() {
        return "TerrainDatabase{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", departement=" + departement +
                ", ville='" + ville + '\'' +
                ", positionMap='" + positionMap + '\'' +
                ", note=" + note +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDepartement() {
        return departement;
    }

    public void setDepartement(int departement) {
        this.departement = departement;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPositionMap() {
        return positionMap;
    }

    public void setPositionMap(String iPositionMap) {
        this.positionMap = iPositionMap;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
