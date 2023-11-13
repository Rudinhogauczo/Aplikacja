package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class Ekranglowny1 : AppCompatActivity() {
    private lateinit var databaseManager: Baza

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranglowny1)

        val intent = intent
        if (intent.hasExtra("IMIE")) {
            val imie = intent.getStringExtra("IMIE")

            // Teraz użyj pobranego imienia, aby pobrać resztę danych z bazy danych
            val dbHelper = Baza(this)
            val db = dbHelper.readableDatabase

            val columnsToRetrieve = arrayOf("wzrost", "waga", "typ_wagi")
            val selection = "imie = ?"
            val selectionArgs = arrayOf(imie)

            val cursor =
                db.query("Konto", columnsToRetrieve, selection, selectionArgs, null, null, null)

            if (cursor.moveToFirst()) {
                val wzrost = cursor.getInt(cursor.getColumnIndex("wzrost"))
                val waga = cursor.getInt(cursor.getColumnIndex("waga"))
                val typWagi = cursor.getString(cursor.getColumnIndex("typ_wagi"))

                // Tutaj możesz używać pobranych danych (np. wyświetlić w interfejsie użytkownika)
                Log.d(
                    "Ekranglowny1",
                    "Imię: $imie, Wzrost: $wzrost, Waga: $waga, Typ Wagi: $typWagi"
                )
            }

            cursor.close()
            db.close()
        }
    }
}