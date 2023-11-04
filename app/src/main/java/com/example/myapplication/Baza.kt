package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

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
    }
}