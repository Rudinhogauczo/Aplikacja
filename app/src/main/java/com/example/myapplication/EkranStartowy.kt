package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class EkranStartowy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranstartowy)

        val przyciskrozpocznij = findViewById<ImageButton>(R.id.PrzyciskRozpocznij)
        val przyciskustawienia = findViewById<ImageButton>(R.id.PrzyciskUstawienia)
        val przyciskoprogramie = findViewById<ImageButton>(R.id.PrzyciskOProgramie)
        val przyciskwyjdz = findViewById<ImageButton>(R.id.PrzyciskWyjdz)

        przyciskrozpocznij.setOnClickListener {
            val intent = Intent(this, EkranGlowny::class.java)
            startActivity(intent)
            finish()
        }

        przyciskustawienia.setOnClickListener {
            val intent = Intent(this, Ustawienia::class.java)
            startActivity(intent)
            finish()
        }

        przyciskoprogramie.setOnClickListener {
            val intent = Intent(this, OProgramie::class.java)
            startActivity(intent)
            finish()
        }

        przyciskwyjdz.setOnClickListener {
            finishAffinity()
        }

    }

}
