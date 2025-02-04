package com.example.tablelayout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var ventanaDeslizante: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var txtTexto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ventanaDeslizante = findViewById(R.id.viewpager)
        tablayout = findViewById(R.id.tabLayout)
        txtTexto = findViewById(R.id.txtTextoInformacion)

        val controlador = ControladorVentanaDeslizante(supportFragmentManager)

        controlador.addFragments(HobbiesFragment(), "Hobbies")
        controlador.addFragments(viajesFragment(), "Viajes")

        ventanaDeslizante.adapter = controlador
        tablayout.setupWithViewPager(ventanaDeslizante)

        tablayout.getTabAt(0)?.setIcon(R.drawable.baseline_casino_24)
        tablayout.getTabAt(1)?.setIcon(R.drawable.baseline_email_24)
    }
}