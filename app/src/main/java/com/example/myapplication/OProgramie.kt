package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class OProgramie : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oprogramie)

        val przyciskwroc2 = findViewById<ImageButton>(R.id.PrzyciskWroc2)

        przyciskwroc2.setOnClickListener {
            val intent = Intent(this, EkranStartowy::class.java)
            startActivity(intent)
            finish()
        }
    }
}