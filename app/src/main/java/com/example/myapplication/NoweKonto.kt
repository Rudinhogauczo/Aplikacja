package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.content.ContentValues


class NoweKonto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nowekonto)

        val dbHelper = Baza(this)

        val spinnerOptions = findViewById<Spinner>(R.id.WyborTypu)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapter.add("Chcę zredukować wagę")
        adapter.add("Chcę zachować wagę")
        adapter.add("Chcę zwiększyć wagę")

        spinnerOptions.adapter = adapter

        val buttonrezygnacja = findViewById<ImageButton>(R.id.ButtonRezygnacja)

        buttonrezygnacja.setOnClickListener {
            val intent = Intent(this, EkranGlowny::class.java)
            startActivity(intent)
        }

        val buttonAkceptacja = findViewById<ImageButton>(R.id.ButtonAkceptacja)
        buttonAkceptacja.setOnClickListener {
            val imieEditText = findViewById<EditText>(R.id.PodajImie)
            val wzrostEditText = findViewById<EditText>(R.id.PodajWzrost)
            val wagaEditText = findViewById<EditText>(R.id.PodajWagę)
            val typWagiSpinner = findViewById<Spinner>(R.id.WyborTypu)

            val imie = imieEditText.text.toString()
            val wzrost = wzrostEditText.text.toString().toInt()
            val waga = wagaEditText.text.toString().toInt()
            val typWagi = typWagiSpinner.selectedItem.toString()

            val contentValues = ContentValues()
            contentValues.put("imie", imie)
            contentValues.put("wzrost", wzrost)
            contentValues.put("waga", waga)
            contentValues.put("typ_wagi", typWagi)

            val db = dbHelper.writableDatabase
            db.insert("Konto", null, contentValues)

            val intent = Intent(this, EkranGlowny::class.java)
            startActivity(intent)
            finish()

        }
    }

}

