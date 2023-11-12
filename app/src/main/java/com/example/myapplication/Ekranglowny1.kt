package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Ekranglowny1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranglowny1)

        // Retrieve data from the database or wherever you store it
        val intent = intent
        val uniqueData = intent.getStringExtra("UNIQUE_DATA")

        // Use the unique data to customize the behavior of this activity
        // ...

        // Get data from the database based on the unique data
        val dbHelper = Baza(this)
        val db = dbHelper.readableDatabase

        // Example query to get the imie column value based on the uniqueData
        val selection = "imie = ?"
        val selectionArgs = arrayOf(uniqueData)
        val cursor = db.query("Konto", arrayOf("imie", "wzrost", "waga", "typ_wagi"), selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val imie = cursor.getString(cursor.getColumnIndex("imie"))
            val wzrost = cursor.getInt(cursor.getColumnIndex("wzrost"))
            val waga = cursor.getInt(cursor.getColumnIndex("waga"))
            val typWagi = cursor.getString(cursor.getColumnIndex("typ_wagi"))

            // Do something with the retrieved data
            // ...

            // Example: Display a toast with the retrieved data
            val toastMessage = "Imię: $imie, Wzrost: $wzrost, Waga: $waga, Typ wagi: $typWagi"
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        cursor.close()
        db.close()
    }
}