package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EkranGlowny : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranglowny)

        val buttondodaj = findViewById<Button>(R.id.ButtonDodaj)

        buttondodaj.setOnClickListener {
            val intent = Intent(this, NoweKonto::class.java)
            startActivity(intent)
        }
    }
}