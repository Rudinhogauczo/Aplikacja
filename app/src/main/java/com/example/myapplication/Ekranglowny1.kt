package com.example.myapplication

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Ekranglowny1 : AppCompatActivity() {
    private lateinit var dbHelper: Baza
    private lateinit var db: SQLiteDatabase
    private lateinit var imie: String
    private var kalorie: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranglowny1)

        dbHelper = Baza(this)
        db = dbHelper.readableDatabase

        val intent = intent
        if (intent.hasExtra("IMIE")) {
            imie = intent.getStringExtra("IMIE") ?: ""
            checkAndResetCaloriesForToday()
            update_kalorie_Display()
        }

        val addButton = findViewById<Button>(R.id.addButton)
        val caloriesEditText = findViewById<EditText>(R.id.caloriesEditText)

        addButton.setOnClickListener {
            val dodaneKalorie = caloriesEditText.text.toString().toIntOrNull() ?: 0
            kalorie += dodaneKalorie

            update_kalorie_w_bazie(kalorie)
            update_kalorie_Display()
        }
    }
    private fun checkAndResetCaloriesForToday() {
        val today = Calendar.getInstance()
        val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)

        val columnsToRetrieve = arrayOf("data_resetu")
        val selection = "imie = ?"
        val selectionArgs = arrayOf(imie)

        val cursor = db.query("Konto", columnsToRetrieve, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val lastResetDate = cursor.getString(cursor.getColumnIndex("data_resetu"))

            if (lastResetDate != todayString) {
                // Resetuje kalorie do zera
                update_kalorie_w_bazie(0)

                // Aktualizuje datę resetu na dzisiaj
                val values = ContentValues()
                values.put("data_resetu", todayString)
                db.update("Konto", values, selection, selectionArgs)
            }
        }

        cursor.close()
    }
    private fun update_kalorie_w_bazie(newCalories: Int) {
        val values = ContentValues()
        values.put("kalorie", newCalories)

        val selection = "imie = ?"
        val selectionArgs = arrayOf(imie)

        db.update("Konto", values, selection, selectionArgs)
    }



    private fun update_kalorie_Display() {
        val columnsToRetrieve = arrayOf("wzrost", "waga", "typ_wagi", "kalorie")
        val selection = "imie = ?"
        val selectionArgs = arrayOf(imie)

        val cursor = db.query("Konto", columnsToRetrieve, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val wzrost = cursor.getInt(cursor.getColumnIndex("wzrost"))
            val waga = cursor.getInt(cursor.getColumnIndex("waga"))
            val typWagi = cursor.getString(cursor.getColumnIndex("typ_wagi"))
            kalorie = cursor.getInt(cursor.getColumnIndex("kalorie"))

            val textViewDane = findViewById<TextView>(R.id.textViewDane)
            val textToShow = "Masz na imię: $imie\nMasz $wzrost cm wzrostu\nMasz $waga kg wagi \nCo chcesz zrobić? $typWagi\nAktualna liczba kalorii: $kalorie"
            textViewDane.text = textToShow
        }

        cursor.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}
