package com.example.tablelayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ControladorRecyclerView(private var contactosList: ArrayList<Contacto>): RecyclerView.Adapter<VistaContacto>() {

    //Este elemento crea un nuevo viewHolder para cada elemento de la lista recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaContacto {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacto_item_layout, parent, false)
        return VistaContacto(view)
    }

    //Este método devuelve el número total de elementos de la lista
    override fun getItemCount(): Int {
        return contactosList.size
    }

    /*Este método vincula los datos de la lista con el onjeto View Holder
    * para que sean mostrados de la forma que se le indica */
    override fun onBindViewHolder(holder: VistaContacto, position: Int) {
        val contacto = contactosList[position]
        holder.nombre?.text = contacto.nombre
        holder.email?.text = contacto.email
        holder.telefono?.text = contacto.telefono
//        holder.foto?.setImageResource(contacto.foto)
    }

    fun actualizarContactos(nuevosContactos: List<Contacto>) {
        this.contactosList = nuevosContactos as ArrayList<Contacto>
        notifyDataSetChanged()
    }
}

//Esta clase define el ViewHolder para cada item de la lista controlador
class VistaContacto(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
    val nombre: TextView? = itemView?.findViewById(R.id.textNombre)
    val email: TextView? = itemView?.findViewById(R.id.textEmail)
    val telefono: TextView? = itemView?.findViewById(R.id.textTelefono)
    val foto: ImageView? = itemView?.findViewById(R.id.imagenContacto)
}