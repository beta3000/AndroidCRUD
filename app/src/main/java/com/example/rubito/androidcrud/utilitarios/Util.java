package com.example.rubito.androidcrud.utilitarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.rubito.androidcrud.dao.UsuariosSQLiteHelper;

/**
 * Created by RUBITO on 19/04/2016.
 */
public class Util {
    public static void message(Context context,String msg){
        int duracion = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,msg,duracion);
        toast.show();
    }


}
