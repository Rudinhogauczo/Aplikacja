package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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
            update_kalorie_Display()
        }

    }

    @SuppressLint("Range")
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

    @SuppressLint("Range")
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
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDate = selectedDateCalendar.time // Konwersja na obiekt Date

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)

                editTextDate.setText(formattedDate)
                displayCaloriesForDate(selectedDate)
            },
            rok,
            miesiac,
            dzien
        )
        datePickerDialog.show()
    }
    @SuppressLint("SetTextI18n", "Range")
    private fun displayCaloriesForDate(selectedDate: Date) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)

        val columnsToRetrieve = arrayOf("kalorie")
        val selection = "imie = ? AND data = ?"
        val selectionArgs = arrayOf(imie, formattedDate) // Wykorzystanie sformatowanej daty

        val cursor = db.query("DziennaKontrolaKalorii", columnsToRetrieve, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val calories = cursor.getInt(cursor.getColumnIndex("kalorie"))
            val textViewCalories = findViewById<TextView>(R.id.textWyswietlkalorie)
            textViewCalories.text = "Calories for $formattedDate: $calories"
        } else {
            val textViewCalories = findViewById<TextView>(R.id.textWyswietlkalorie)
            textViewCalories.text = "No data available for $formattedDate"
        }

        cursor.close()
    }
    private fun saveCaloriesToDatabase() {
        val today = Calendar.getInstance()
        val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)

        val values = ContentValues()
        values.put("imie", imie)
        values.put("data", todayString)
        values.put("kalorie", kalorie)

        val selection = "imie = ? AND data = ?"
        val selectionArgs = arrayOf(imie, todayString)

        val rowsAffected = db.update("DziennaKontrolaKalorii", values, selection, selectionArgs)

        if (rowsAffected <= 0) {
            val insertedRowId = db.insert("DziennaKontrolaKalorii", null, values)

            if (insertedRowId != -1L) {
                Log.d("Database", "Calorie data inserted successfully")
            } else {
                Log.e("Database", "Failed to insert calorie data")
            }
        } else {
            Log.d("Database", "Calorie data updated successfully")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveCaloriesToDatabase()
        dbHelper.close()
    }

    override fun onPause() {
        super.onPause()
        saveCaloriesToDatabase()
        // Close the writable database


    }
}
