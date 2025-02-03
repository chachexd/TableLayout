package com.example.tablelayout.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tablelayout.Contacto;

import java.util.ArrayList;

public class DBConexion extends SQLiteOpenHelper {
    private static final String DB_NAME = "aplicacionDB";
    private static final int DB_VERSION = 2;

    //Tabla contactos
    private static final String TABLA_CONTACTOS = "contactos";
    private static final String CONTACTO_ID = " _id";
    private static final String CONTACTO_NOMBRE = "nombre";
    private static final String CONTACTO_TLFNO = "telefono";
    private static final String CONTACTO_EMAIL = "email";
    private static final String SENTENCIA_CREACION_TABLA_CONTACTOS = "create table contactos" +
            "(_id integer not null, nombre text not null, telefono text not null, email text not null);";

    private static final String SENTENCIA_ACTUALIZACION_TABLA_CONTACTOS = "UPDATE contactos";

    private static final String SENTENCIA_SELECCION_CONTACTOS = "select * from contactos";

    public static final String SENTENCIA_INSERCION_CONTACTOS = "insert into contactos (nombre, telefono, email)";

    // Define the callback interface
    public interface OnContactInsertedListener {
        void onContactInserted();
    }

    private OnContactInsertedListener contactInsertedListener;

    // Method to set the callback listener
    public void setOnContactInsertedListener(OnContactInsertedListener listener) {
        this.contactInsertedListener = listener;
    }

    //Constructor
    public DBConexion(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Código sql
        //Instanciamos y creamos la base de datos
        //Este código se ejcuta cuando se crea la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONTACTOS);
        db.execSQL(SENTENCIA_CREACION_TABLA_CONTACTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Código sql para actualizar la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONTACTOS);
        onCreate(db);
    }

    public ArrayList<Contacto> selectContactos (SQLiteDatabase db) {
        ArrayList<Contacto> contactos = new ArrayList<>();

        //Consultamos los datos
        Cursor c = db.rawQuery(SENTENCIA_SELECCION_CONTACTOS, null);

        if (c.moveToFirst()) {
            do { //Es un fetch Array, ya utilizado en SQL Statement
//                @SuppressLint("Range");
//                int id = c.getInt(c.getColumnIndex("_id"));

                //Asignamos el valor en nuestras variables para usarlos en lo que necesitemos
                @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex("nombre"));
                @SuppressLint("Range") String telefono = c.getString(c.getColumnIndex("telefono"));
                @SuppressLint("Range") String email = c.getString(c.getColumnIndex("email"));

                Contacto contactoExtraidoBD = new Contacto(nombre, email, telefono, 1);
                contactos.add(contactoExtraidoBD);
            } while (c.moveToNext());
        }

        c.close();
        return contactos;
    }

    public void insertarContacto (SQLiteDatabase db, Contacto contacto) {
        ContentValues valores = new ContentValues();
        valores.put("_id", 1);
        valores.put("nombre", contacto.getNombre());
        valores.put("telefono", contacto.getTelefono());
        valores.put("email", contacto.getEmail());

        db.insert(TABLA_CONTACTOS, null, valores);

        // Notify the listener
        if (contactInsertedListener != null) {
            contactInsertedListener.onContactInserted();
        }
    }
}