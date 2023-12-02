package com.example.myapplication

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EkranKonta : AppCompatActivity() {
    private lateinit var dbHelper: Baza
    private lateinit var db: SQLiteDatabase
    private lateinit var imie: String
    private var kalorie: Int = 0
    private lateinit var editTextDate: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekrankonta)

        dbHelper = Baza(this)
        db = dbHelper.readableDatabase

        val intent = intent
        if (intent.hasExtra("IMIE")) {
            imie = intent.getStringExtra("IMIE") ?: ""
            resetuj_kalorie()
            update_kalorie_Display()
        }

        val addButton = findViewById<Button>(R.id.addButton)
        val caloriesEditText = findViewById<EditText>(R.id.caloriesEditText)
        editTextDate = findViewById(R.id.editTextDate)
        editTextDate.setOnClickListener {
            funkcja_Kalendarz()
        }
        addButton.setOnClickListener {
            val enteredCalories = caloriesEditText.text.toString().toIntOrNull() ?: 0
            kalorie += enteredCalories

            update_kalorie_w_bazie(kalorie)
            zapisz_kalorie(imie, enteredCalories)
            update_kalorie_Display()
        }


    }
    private fun resetuj_kalorie() {
        val today = Calendar.getInstance()
        val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)

        val columnsToRetrieve = arrayOf("data_resetu")
        val selection = "imie = ?"
        val selectionArgs = arrayOf(imie)

        val cursor = db.query("Konto", columnsToRetrieve, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val lastResetDate = cursor.getString(cursor.getColumnIndex("data_resetu"))

            if (lastResetDate != todayString) {
                update_kalorie_w_bazie(0)

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
    private fun zapisz_kalorie(imie: String, enteredCalories: Int) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

        val values = ContentValues().apply {
            put("imie", imie)
            put("data", today)
            put("ilosc_kalorii",enteredCalories)
        }

        db.insert("DziennaKontrolaKalorii", null, values)
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
    private fun funkcja_Kalendarz() {
        val kalendarz = Calendar.getInstance()
        val rok = kalendarz.get(Calendar.YEAR)
        val miesiac = kalendarz.get(Calendar.MONTH)
        val dzien = kalendarz.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                editTextDate.setText(selectedDate)

                kalorie_z_daty(selectedDate)
            },
            rok,
            miesiac,
            dzien
        )
        datePickerDialog.show()
    }
    private fun kalorie_z_daty(date: String) {
        Log.d("DEBUG", "Wyświetlanie kalorii dla daty: $date") // Sprawdzenie daty
        val columnsToRetrieve = arrayOf("SUM(ilosc_kalorii) AS total_calories") // Zapytanie o sumę kalorii
        val selection = "imie = ? AND data = ?"
        val selectionArgs = arrayOf(imie, date)

        val cursor = db.query(
            "DziennaKontrolaKalorii",
            columnsToRetrieve,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var totalCalories = 0 // Domyślna wartość

        if (cursor != null && cursor.moveToFirst()) {
            totalCalories = cursor.getInt(cursor.getColumnIndex("total_calories"))
        }

        cursor.close()

        val textView = findViewById<TextView>(R.id.textWyswietlkalorie)
        textView.text = "Spożyte kalorie w dniu $date: $totalCalories"
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}
