package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class EkranGlowny : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranglowny)

        val buttonDodaj = findViewById<Button>(R.id.ButtonDodaj)
        val buttonReset = findViewById<Button>(R.id.ButtonReset)
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)


        buttonDodaj.setOnClickListener {
            val intent = Intent(this, NoweKonto::class.java)
            startActivity(intent)
        }

        buttonReset.setOnClickListener {
            val dbHelper = Baza(this)
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM Konto")
            db.close()


            gridLayout.removeAllViews()
        }

        val dbHelper = Baza(this)
        val db = dbHelper.readableDatabase
        val cursor = db.query("Konto", arrayOf("imie"), null, null, null, null, null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val imie = cursor.getString(cursor.getColumnIndex("imie"))

                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.VERTICAL

                val ButtonKonto = ImageButton(this)
                ButtonKonto.setBackgroundResource(0)
                ButtonKonto.setImageResource(R.drawable.konto100x100)

                // Dodaj obsługę kliknięcia dla przycisku
                ButtonKonto.setOnClickListener {
                    // Przygotuj dane do przekazania do NoweKontoActivity
                    val intent = Intent(this@EkranGlowny, Ekranglowny1::class.java)
                    intent.putExtra("imie", imie)
                    startActivity(intent)
                }

                val textViewImie = TextView(this)
                textViewImie.text = imie
                textViewImie.gravity = Gravity.CENTER

                linearLayout.addView(ButtonKonto)
                linearLayout.addView(textViewImie)

                gridLayout.addView(linearLayout)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
}

