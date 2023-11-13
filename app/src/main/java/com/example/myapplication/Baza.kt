package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import java.io.Serializable

class Baza(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS Konto (id INTEGER PRIMARY KEY, imie TEXT, wzrost INTEGER, waga INTEGER, typ_wagi TEXT)"
        db.execSQL(createTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Konto")
        onCreate(db)
    }



    companion object {
        private const val DATABASE_NAME = "BazaDanych"
        private const val DATABASE_VERSION = 1
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
        val typWagi: String
    ) : Serializable
        {
            companion object {
                // Tu możesz umieszczać metody lub stałe związane z klasą Konto
            }
        }
}

