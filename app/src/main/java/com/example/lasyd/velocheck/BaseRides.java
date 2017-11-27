package com.example.lasyd.velocheck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by lasyd on 11/22/2016.
 */

public class BaseRides extends SQLiteOpenHelper implements BaseColumns {

    // версия базы
    private static final int DATABASE_VERSION = 3;

    // имя базы
    private static final String DATABASE_NAME = "velocheck.db";

    // Table Names
    static final String TABLE_LOC = "Location";
    static final String TABLE_RIDE = "Rides";

    public final static String _ID = BaseColumns._ID;
    //название столбцов для табл 1
    //от датчиков
    public static final String LATITUDE_COLUMN = "Latitude";
    public static final String LONGITUDE_COLUMN = "Longitude";
    public static final String ALTITUDE_COLUMN = "Altitude";
    public static final String SYSVELOCITY_COLUMN = "SysVelocity";
    public static final String PCTIME_COLUMN = "PCtime";

    //производные
    public static final String DPCTIME_COLUMN = "DPCtime";
    public static final String DLATITUDE_COLUMN = "DLatitude";
    public static final String DLONGITUDE_COLUMN = "DLongitude";
    public static final String DSYSVELOCITY_COLUMN = "DSysVelocity";
    public static final String DALTITUDE_COLUMN = "DAltitude";
    public static final String ESTIMATEDVELOSITY_COLUMN = "EstimatedVelocity";

    //название столбцов для табл 1
    public static final String RIDE_START="Start";
    public static final String PCTIME_COLUMN2 = "PCtime";

    public static final String CREATE_TABLE_LOC="CREATE TABLE "+TABLE_LOC+"(" +_ID + " INTEGER PRIMARY KEY, " + LATITUDE_COLUMN
            + " double, " + LONGITUDE_COLUMN + " double, " + ALTITUDE_COLUMN + " double, " + SYSVELOCITY_COLUMN + " double, " + PCTIME_COLUMN
            + " double, " + DPCTIME_COLUMN + " double, " + DLATITUDE_COLUMN + " double, " + DLONGITUDE_COLUMN + " double, " + DSYSVELOCITY_COLUMN +
            " double, " + DALTITUDE_COLUMN + " double " + ESTIMATEDVELOSITY_COLUMN + " double );";

    public static final String CREATE_TABLE_RIDE="CREATE TABLE "+TABLE_RIDE+"(" +_ID + " INTEGER PRIMARY KEY, " + RIDE_START + "INTEGER, " + PCTIME_COLUMN2 + " double );";

    public BaseRides(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOC);
        db.execSQL(CREATE_TABLE_RIDE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_LOC);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_RIDE);
        // Создаём новую таблицу
        onCreate(db);
    }


}
