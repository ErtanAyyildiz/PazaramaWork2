package com.example.androidcase2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DBGateway(context: Context?) :
    SQLiteOpenHelper(context, "YedekParca.db", null, 1 )
{
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("Create Table Kategoriler(ID Integer PRIMARY KEY AUTOINCREMENT, Aciklama TEXT);")
        db!!.execSQL("Create Table Parcalar(ID Integer PRIMARY KEY AUTOINCREMENT, KategoriID Integer, Adi Text, StokAdedi Integer, Fiyati Integer);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}