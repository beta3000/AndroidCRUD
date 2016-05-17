package com.example.rubito.androidcrud;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rubito.androidcrud.dao.UsuariosSQLiteHelper;
import com.example.rubito.androidcrud.utilitarios.Constantes;
import com.example.rubito.androidcrud.utilitarios.Util;

public class CrudActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBD;
    private Button btnAgregar;
    //LISTAR
    private Button btnListar;
    private TextView textResult;

    //DELETE
    private EditText editDelete;
    private Button btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
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

        btnBD = (Button) findViewById(R.id.btnBD);
        btnBD.setOnClickListener(this);

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(this);

        btnListar = (Button) findViewById(R.id.btnListar);
        btnListar.setOnClickListener(this);

        textResult = (TextView) findViewById(R.id.textResult);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crud, menu);
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
    public void onClick(View v) {
        Intent i ;
        switch (v.getId()){
            case R.id.btnBD:
                String msg = createDB();
                Util.message(this,msg);
               break;
            case R.id.btnAgregar:
                i = new Intent(this,AgregarActivity.class);
                startActivity(i);
                break;
            case R.id.btnListar:
                textResult.setText("");
                Cursor c = listarUsuarios(conexionDB());
                if (c.moveToFirst()){
                    do {
                        textResult.append(c.getInt(0)+" - "+c.getString(1)+"\n");
                    }while (c.moveToNext());
                }
                break;
            case R.id.btnDelete:
                editDelete = (EditText) findViewById(R.id.editDelete);
                Integer idUsuario = Integer.parseInt(editDelete.getText().toString());
                String msgDelete = eliminarUsuarioPorID(conexionDB(),idUsuario);
                Util.message(this,msgDelete);

                if (msgDelete.equalsIgnoreCase(Constantes.MSG_SUCCESS_DELETE)){
                    editDelete.setText("");
                }
                break;
            default:
                break;
        }

    }

    private String createDB(){
        String msg = "";
        try {
            UsuariosSQLiteHelper userSql = new UsuariosSQLiteHelper(this,"DBUSER",null,1);
            SQLiteDatabase conex = userSql.getWritableDatabase();
            conex.close();
            msg = Constantes.MSG_SUCCESS;
        }catch (Exception e){
            msg = Constantes.MSG_ERROR+" "+e;
        }
        return msg;
    }

    private SQLiteDatabase conexionDB(){
        UsuariosSQLiteHelper helper = new UsuariosSQLiteHelper(this,"DBUser",null,1);
        SQLiteDatabase conex = helper.getWritableDatabase();
        return conex;
    }

    private Cursor listarUsuarios(SQLiteDatabase access){
        String[] camposDevolver = new String[]{"codigo","nombre"};
        Cursor c = access.query("USER", camposDevolver,null,null,null,null,null,"5");
        
        return c;
    }

    private String eliminarUsuarioPorID(SQLiteDatabase access, Integer idUsuario){
        String msgRespuesta ="";
        try {
            access.delete("USER", "codigo ="+idUsuario, null);
            msgRespuesta = Constantes.MSG_SUCCESS_DELETE;
        }catch (Exception e){
            msgRespuesta = Constantes.MSG_ERROR + e;
        }
        return  msgRespuesta;
    }
}
