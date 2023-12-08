package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable

class Baza(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS Konto (id INTEGER PRIMARY KEY, imie TEXT, wzrost INTEGER, waga INTEGER, typ_wagi TEXT, kalorie INTEGER, data_resetu TEXT)"
        db.execSQL(createTableQuery)

        val createDailyCaloriesTableQuery = "CREATE TABLE IF NOT EXISTS DziennaKontrolaKalorii (id INTEGER PRIMARY KEY, imie TEXT, data DATE, kalorie INTEGER)"
        db.execSQL(createDailyCaloriesTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Konto")
        db.execSQL("DROP TABLE IF EXISTS DziennaKontrolaKalorii")
        onCreate(db)
    }



    companion object {
        private const val DATABASE_NAME = "BazaDanych"
        private const val DATABASE_VERSION = 5
        private const val TABLE_NAME = "Konto"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "imie"
        private const val COLUMN_HEIGHT = "wzrost"
        private const val COLUMN_WEIGHT = "waga"
        private const val COLUMN_WEIGHT_TYPE = "typ_wagi"

    }
        data class Konto(
        val id: Long,
        val imie: String,
        val wzrost: Int,
        val waga: Int,
        val typWagi: String,
        val kalorie: Int,

    ) : Serializable
        {
            companion object {
            }
        }
}

