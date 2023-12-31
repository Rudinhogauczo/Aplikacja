package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val siatkakont = findViewById<GridLayout>(R.id.siatkakont)


        buttonDodaj.setOnClickListener {
            val intent = Intent(this, NoweKonto::class.java)
            startActivity(intent)
            finish()
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

                ButtonKonto.setOnClickListener {
                    Log.d("EkranGlowny", "ButtonKonto clicked")

                    val intent = Intent(this, EkranKonta::class.java)

                    // Pobierz ID konta klikniętego przez użytkownika
                    intent.putExtra("IMIE", imie)

                    startActivity(intent)
                }

                val textViewImie = TextView(this)
                textViewImie.text = imie
                textViewImie.gravity = Gravity.CENTER

                linearLayout.addView(ButtonKonto)
                linearLayout.addView(textViewImie)

                siatkakont.addView(linearLayout)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
}



