package com.example.tablelayout

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tablelayout.DB.DBConexion

class agregarContacto : AppCompatActivity() {

    private lateinit var botonAceptar: Button
    private lateinit var botonCancelar: Button
    private lateinit var txtNombre: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtTelefono: EditText

    //Base de datos
    var conexion: DBConexion? = null
    var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_contacto)

        //Obtiene las referencias de los objetos del layout
        txtNombre = findViewById(R.id.txtNombre)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtEmail = findViewById(R.id.txtEmail)
        botonAceptar = findViewById(R.id.botonAceptarAgregar)
        botonCancelar = findViewById(R.id.botonCancelarAgregar)

        //Crea el objeto conexión con la ventana actual
        /* Estas sentencias de conexión y obtención de la base de datos se repiten
        *  en todas las ventanas sobre las que se va a operar en la bbdd */
        conexion = DBConexion(this)
        db = conexion!!.writableDatabase

        botonAceptar.setOnClickListener {
            val intent = Intent()
            //Creo un nuevo objeto contacto tomando los valores escritos en los campos de texto
            val contactoNuevo = Contacto(txtNombre.text.toString(), txtEmail.text.toString(), txtTelefono.text.toString(), 0)

            //Intenta insertarlos en la base de datos el objeto contactoNuevo
            if(db != null) {
                conexion!!.insertarContacto(db, contactoNuevo)
            }

            //Muestra un aviso de que se ha añadido un nuevo contacto
            Toast.makeText(this, "Se ha añadido un nuevo contacto", Toast.LENGTH_LONG).show()
            intent.putExtra("Nombre", txtNombre.toString())
            intent.putExtra("Email", txtEmail.toString())
            intent.putExtra("Telefono", txtTelefono.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        botonCancelar.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }
}