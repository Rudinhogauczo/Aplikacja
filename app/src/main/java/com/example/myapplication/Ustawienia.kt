package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Ustawienia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ustawienia)

        val przyciskresetaplikacji = findViewById<ImageButton>(R.id.PrzyciskResetAplikacji)
        val przyciskwroc = findViewById<ImageButton>(R.id.PrzyciskWroc)

        przyciskresetaplikacji.setOnClickListener {
            val dbHelper = Baza(this)
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM Konto")
            db.close()
        }

        przyciskwroc.setOnClickListener {
            val intent = Intent(this, EkranStartowy::class.java)
            startActivity(intent)
            finish()
        }
    }
}