package com.indigo.smt.dbHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.indigo.smt.objetos.t001_empresa
import com.indigo.smt.objetos.t002_usuarios

class DbHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VER) {

    companion object {
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "smt.db"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        try{
            val CREATE_TABLE_t001_empresa: String =
                ("CREATE TABLE t001_empresa (f001_id INTEGER PRIMARY KEY AUTOINCREMENT,f001_nombre TEXT,f001_logo TEXT, f001_estado TEXT)")

            db!!.execSQL(CREATE_TABLE_t001_empresa);


            val CREATE_TABLE_t002_usuarios: String =
                ("CREATE TABLE t002_usuarios (f002_id INTEGER PRIMARY KEY AUTOINCREMENT,f002_login TEXT,f002_nickname TEXT, f002_estado TEXT)")

            db!!.execSQL(CREATE_TABLE_t002_usuarios);

        } catch (ex: Exception) {
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS t001_empresa")
        onCreate(db!!)
    }


    fun insert_t001_empresa(obj_t001_empresa:t001_empresa){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put("f001_id","1")
        values.put("f001_nombre",obj_t001_empresa.f001_nombre)
        values.put("f001_logo",obj_t001_empresa.f001_logo)
        db.replace("t001_empresa",null,values)

    }

    fun insert_t002_usuarios(obj_t002_usuario: t002_usuarios) {

        val db = this.writableDatabase
        val values = ContentValues()

        values.put("f002_id","1")
        values.put("f002_login",obj_t002_usuario.f002_login)
        values.put("f002_nickname",obj_t002_usuario.f002_nickname)
        values.put("f002_estado",obj_t002_usuario.f002_estado)
        db.replace("t002_usuarios",null,values)

    }

    @SuppressLint("Range")
    fun select_t001_empresa():t001_empresa {
        val obj_t001_empresa=t001_empresa()

        try{

        val selectQuery = "SELECT * FROM t001_empresa where f001_id = '1'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery,null)

        if(cursor.moveToFirst()){
            do {
                obj_t001_empresa.f001_estado = cursor.getString(cursor.getColumnIndex("f001_estado"))
                obj_t001_empresa.f001_nombre = cursor.getString(cursor.getColumnIndex("f001_nombre"))
                obj_t001_empresa.f001_logo = cursor.getString(cursor.getColumnIndex("f001_logo"))

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        }catch (e:Exception){
            println(e.message)
        }

        return obj_t001_empresa
    }

    @SuppressLint("Range")
    fun select_t002_usuarios():t002_usuarios {
        val obj_t002_usuario=t002_usuarios()
        try{
            val selectQuery = "SELECT * FROM t002_usuarios where f002_id = '1'"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery,null)

            if(cursor.moveToFirst()){
                do {

                    obj_t002_usuario.f002_login = cursor.getString(cursor.getColumnIndex("f002_login"))
                    obj_t002_usuario.f002_nickname = cursor.getString(cursor.getColumnIndex("f002_nickname"))
                    obj_t002_usuario.f002_estado = cursor.getString(cursor.getColumnIndex("f002_estado"))

                }while (cursor.moveToNext())
            }
            cursor.close()
            db.close()

        }catch (e:Exception){
            println(e.message)
        }

        return obj_t002_usuario
    }

}