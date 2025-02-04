package com.example.tablelayout

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tablelayout.DB.DBConexion

class HobbiesFragment : Fragment(), DBConexion.OnContactInsertedListener {
    protected var mRecyclerView: RecyclerView? = null
    protected var mAdapter: ControladorRecyclerView? = null
    protected var mLayoutManager: RecyclerView.LayoutManager? = null
    lateinit var recyclerView: RecyclerView
    lateinit var btnNuevoHobby: Button

    var conexion1: DBConexion? = null
    var db: SQLiteDatabase? = null
    var listaHobbies = ArrayList<Contacto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conexion1 = DBConexion(requireContext())
        db = conexion1!!.writableDatabase
        if (db != null) {
            inciarRecogidaDatos(conexion1, db)
        }

        // Set the callback listener
        conexion1!!.setOnContactInsertedListener(this)
    }

    override fun onResume() {
        super.onResume()
        conexion1 = DBConexion(requireContext())
        db = conexion1!!.writableDatabase
        if (db!= null) {
            inciarRecogidaDatos(conexion1, db)
        }
        mAdapter = ControladorRecyclerView(listaHobbies)
        recyclerView.adapter = mAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_hobbies, container, false)
        rootView.tag = "HobbiesFragment"

        btnNuevoHobby = rootView.findViewById(R.id.btnNuevoHobby)
        btnNuevoHobby.setOnClickListener {
            val intent = Intent(this.context, agregarContacto::class.java)
            startActivity(intent)
        }

        mAdapter = ControladorRecyclerView(listaHobbies)
        recyclerView = rootView.findViewById(R.id.recycleViewListaHobbies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter

        // Attach ItemTouchHelper to RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        private val background = ColorDrawable(Color.RED)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val contacto = listaHobbies[position]
            listaHobbies.removeAt(position)
            mAdapter?.notifyItemRemoved(position)
            // Remove the contact from the database
            conexion1?.eliminarContacto(db, contacto)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            if (dX > 0) { // Swiping to the right
                background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }
            background.draw(c)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun inciarRecogidaDatos(conexion: DBConexion?, db: SQLiteDatabase?) {
        if (conexion != null) {
            listaHobbies = conexion.selectContactos(db) as ArrayList<Contacto>
        }
    }

    override fun onContactInserted() {
        // Update the hobbies list
        actualizarListaHobbies()
    }

    private fun actualizarListaHobbies() {
        if (conexion1 != null && db != null) {
            listaHobbies = conexion1!!.selectContactos(db) as ArrayList<Contacto>
            mAdapter?.actualizarContactos(listaHobbies)
        }
    }
}