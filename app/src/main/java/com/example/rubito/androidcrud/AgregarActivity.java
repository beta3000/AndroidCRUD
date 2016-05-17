package com.example.rubito.androidcrud;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rubito.androidcrud.dao.UsuariosSQLiteHelper;
import com.example.rubito.androidcrud.utilitarios.Constantes;
import com.example.rubito.androidcrud.utilitarios.Util;

public class AgregarActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editUser;
    private EditText editemail;
    private EditText editPhone;
    private Button btnSave;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.btnSave:
                int maxId = 0;
                Cursor c = selectMaxId(conexionDB());

                if(c.moveToFirst()){
                    do{
                        maxId = c.getInt(0);
                        if(maxId == 0){
                            maxId = 1;
                        }
                    }while (c.moveToNext());
                }

                editUser = (EditText) findViewById(R.id.editTextUser);
                editemail = (EditText) findViewById(R.id.editTextEmail);
                editPhone = (EditText) findViewById(R.id.editTextPhone);

                ContentValues usuario = new ContentValues();
                usuario.put("codigo", maxId);
                /*LA TABLA PRESENTA NOT NULL PARA LOS SIGUIENTES CAMPOS*/
                usuario.put("nombre", editUser.getText().toString().equalsIgnoreCase("") ? null : editUser.getText().toString());
                usuario.put("email",editemail.getText().toString().equalsIgnoreCase("") ? null : editemail.getText().toString());
                usuario.put("telefono",editPhone.getText().toString().equalsIgnoreCase("") ? null : editPhone.getText().toString());

                String msg = saveUsuario(usuario,conexionDB());

                //LIMPIA LOS EDITTEXT AL SABER QUE EL REGISTRO FUE CORRECTO
                if(msg.equalsIgnoreCase(Constantes.MSG_SUCCESS)){
                    editUser.setText("");
                    editemail.setText("");
                    editPhone.setText("");
                }

                //REENVIO DIRECTO AL CRUD ACTIVITY AL SABER QUE EL REGISTRO FUE CORRECTO
                /*
                if(msg.equalsIgnoreCase(Constantes.MSG_SUCCESS)){
                    i = new Intent(this,CrudActivity.class);
                    startActivity(i);
                }
                */

                Util.message(this,msg);
                break;
            case R.id.btnRegresar:
                i = new Intent(this,CrudActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private Cursor selectMaxId(SQLiteDatabase access){
        Cursor c = access.rawQuery("SELECT MAX(codigo)+1 FROM USER", null);
        return c;
    }

    private SQLiteDatabase conexionDB(){
        UsuariosSQLiteHelper helper = new UsuariosSQLiteHelper(this,"DBUser",null,1);
        SQLiteDatabase conex = helper.getWritableDatabase();
        return conex;
    }

    private String saveUsuario(ContentValues nuevoUsuario, SQLiteDatabase access){
        String result = "";

        try {
            access.insertOrThrow("USER","nombre",nuevoUsuario);
            access.close();
            result = Constantes.MSG_SUCCESS;
        }catch (Exception e){
            result = Constantes.MSG_ERROR+" "+e;
        }
        return result;
    }
}
