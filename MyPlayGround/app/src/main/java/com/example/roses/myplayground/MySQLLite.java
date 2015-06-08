package com.example.roses.myplayground;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by roses on 16/12/14.
 */
public class MySQLLite extends SQLiteOpenHelper {

    // Books table name
    private static final String TABLE_TERRAINS = "terrains";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_DEPARTEMENT = "departement";
    private static final String KEY_VILLE = "ville";
    private static final String KEY_POSITION_MAP = "positionMap";
    private static final String KEY_NOTE = "note";



    private static final String[] COLUMNS = {KEY_ID,KEY_NOM,KEY_LATITUDE,KEY_LONGITUDE,KEY_DEPARTEMENT,KEY_VILLE,KEY_POSITION_MAP, KEY_NOTE};

    // Database Version
    private static final int DATABASE_VERSION = 8;

    // Database Name
    private static final String DATABASE_NAME = "TerrainDB";

    public MySQLLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_TERRAIN_TABLE = "CREATE TABLE terrains ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT, "+
                "latitude DOUBLE, " +
                "longitude DOUBLE, " +
                "departement INT, " +
                "ville TEXT, " +
                "positionMap TEXT, " +
                "note DOUBLE " +
                " )";

        // create books table);
        db.execSQL(CREATE_TERRAIN_TABLE);

        Log.d("OK : ", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS terrains");

        // create fresh books table
        this.onCreate(db);
        Log.d("OK : ", "onUpgrade");
    }

    public void addTerrain(TerrainDatabase terrain){
        //for logging
        Log.d("addTerrain", terrain.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, terrain.getNom());
        values.put(KEY_LATITUDE, terrain.getLatitude());
        values.put(KEY_LONGITUDE, terrain.getLongitude());
        values.put(KEY_DEPARTEMENT, terrain.getDepartement());
        values.put(KEY_VILLE, terrain.getVille());
        values.put(KEY_POSITION_MAP, terrain.getPositionMap());
        values.put(KEY_NOTE, terrain.getNote());
        // 3. insert
        db.insert(TABLE_TERRAINS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public TerrainDatabase getTerrain(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setId(Integer.parseInt(cursor.getString(0)));
        terrain.setNom(cursor.getString(1));
        terrain.setLatitude(cursor.getDouble(2));
        terrain.setLongitude(cursor.getDouble(3));
        terrain.setDepartement(cursor.getInt(4));
        terrain.setVille(cursor.getString(5));
        terrain.setPositionMap(cursor.getString(6));
        terrain.setNote(cursor.getDouble(7));

        //log
        Log.d("getTerrain("+id+")", terrain.toString());

        cursor.close();
        // 5. return terrain
        return terrain;
    }

    /** Personal method */
    public TerrainDatabase getTerrainWithHisName(String id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " nom = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setId(Integer.parseInt(cursor.getString(1)));
        terrain.setNom(cursor.getString(1));
        terrain.setLatitude(cursor.getDouble(2));
        terrain.setLongitude(cursor.getDouble(3));
        terrain.setDepartement(cursor.getInt(4));
        terrain.setVille(cursor.getString(5));
        terrain.setPositionMap(cursor.getString(6));
        terrain.setNote(cursor.getDouble(7));

        //log
        Log.d("getTerrain("+id+")", terrain.toString());

        cursor.close();
        // 5. return terrain
        return terrain;

    }

    public List<TerrainDatabase> getAllTerrains() {
        List<TerrainDatabase> terrains = new LinkedList<TerrainDatabase>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TERRAINS + " LIMIT 10";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        TerrainDatabase terrain = null;
        if (cursor.moveToFirst()) {
            do {
                terrain = new TerrainDatabase();
                terrain.setId(Integer.parseInt(cursor.getString(0)));
                terrain.setNom(cursor.getString(1));
                terrain.setLatitude(cursor.getDouble(2));
                terrain.setLongitude(cursor.getDouble(3));
                terrain.setDepartement(cursor.getInt(4));
                terrain.setVille(cursor.getString(5));
                terrain.setPositionMap(cursor.getString(6));
                terrain.setNote(cursor.getDouble(7));

                // Add terrain to terrains
                terrains.add(terrain);
            } while (cursor.moveToNext());
        }

        Log.d("getAllTerrains()", terrains.toString());

        // return books
        return terrains;
    }

    public int updateTerrain(TerrainDatabase terrain) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("nom", terrain.getNom());
        values.put("latitude", terrain.getLatitude());
        values.put("longitude", terrain.getLongitude());
        values.put("departement", terrain.getDepartement());
        values.put("ville", terrain.getVille());
        values.put("note", terrain.getNote());
        values.put("positionMap", terrain.getPositionMap());

        // 3. updating row
        int i = db.update(TABLE_TERRAINS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(terrain.getId()) }); //selection args

        // 4. close
        db.close();

        return i;
    }

    /** Personal method **/
    public String getName(){
        String name = "";

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        null, // c. selections
                        new String[] { }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setNom(cursor.getString(1));

        name = terrain.getNom();

        return name;
    }

    /** Personal method */
    public Location getPosition(String nom){
        Location positionTerrain = new Location("");
        double latitude, longitude;

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " NOM = ?", // c. selections
                        new String[]{String.valueOf(nom)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setLatitude(cursor.getDouble(2));
        terrain.setLongitude(cursor.getDouble(3));

        latitude = terrain.getLatitude();
        longitude = terrain.getLongitude();
        positionTerrain.setLatitude(latitude);
        positionTerrain.setLongitude(longitude);

        return positionTerrain;
    }

    public String getAdresseTerrain(String nom){
        String adresseDuTerrain;

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " NOM = ?", // c. selections
                        new String[] { String.valueOf(nom) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
       // if (cursor != null)
        cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setPositionMap(cursor.getString(6));
        terrain.setDepartement(cursor.getInt(4));

        adresseDuTerrain = terrain.getPositionMap();

        return adresseDuTerrain;
    }

    public int getDepartement(String nom){
        int departement;

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " NOM = ?", // c. selections
                        new String[] { String.valueOf(nom) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setDepartement(cursor.getInt(4));

        departement = terrain.getDepartement();

        return departement;
    }

    public String getNomVille(String nom){
        String ville;
        TerrainDatabase terrain = new TerrainDatabase();

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " NOM = ?", // c. selections
                        new String[] { String.valueOf(nom) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if ((cursor != null)  && cursor.moveToFirst()){
            // 4. build book object
            terrain.setVille(cursor.getString(5));
        };

        ville = terrain.getVille();
        return ville;
    }

    public double getNote(String nom){
        double note;

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TERRAINS, // a. table
                        COLUMNS, // b. column names
                        " NOM = ?", // c. selections
                        new String[] { String.valueOf(nom) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TerrainDatabase terrain = new TerrainDatabase();
        terrain.setNote(cursor.getDouble(7));

        note = terrain.getNote();

        return note;
    }

    public void deleteTerrain(TerrainDatabase terrain) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TERRAINS, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(terrain.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteTerrain", terrain.toString());

    }
}
