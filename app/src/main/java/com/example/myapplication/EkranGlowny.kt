package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout

class EkranGlowny : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranglowny)

        val buttonDodaj = findViewById<Button>(R.id.ButtonDodaj)
        val buttonReset = findViewById<Button>(R.id.ButtonReset)
        val linearImiona = findViewById<LinearLayout>(R.id.linearImiona)


        buttonDodaj.setOnClickListener {
            val intent = Intent(this, NoweKonto::class.java)
            startActivity(intent)
        }

        buttonReset.setOnClickListener {
            val dbHelper = Baza(this)
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM Konto")
            db.close()


            linearImiona.removeAllViews()
        }

        val dbHelper = Baza(this)
        val db = dbHelper.readableDatabase
        val cursor = db.query("Konto", arrayOf("imie"), null, null, null, null, null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val imie = cursor.getString(cursor.getColumnIndex("imie"))

                val ButtonKonto = ImageButton(this)
                ButtonKonto.setImageResource(R.drawable.konto100x100)
                linearImiona.addView(ButtonKonto)
                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
}

