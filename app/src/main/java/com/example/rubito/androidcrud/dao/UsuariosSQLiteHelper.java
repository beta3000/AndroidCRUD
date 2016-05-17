package com.example.rubito.androidcrud.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RUBITO on 19/04/2016.
 */
public class UsuariosSQLiteHelper extends SQLiteOpenHelper{

    private String sqlCreate = "CREATE TABLE USER(codigo INTEGER, nombre TEXT NOT NULL, email TEXT NOT NULL, telefono TEXT NOT NULL)";

    public UsuariosSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL(sqlCreate);
        //No debería ser así, se debe migrar los datos
    }
}
