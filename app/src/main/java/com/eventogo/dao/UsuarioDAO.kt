package com.eventogo.dao

import android.content.Context
import com.eventogo.database.DBHelper
import com.eventogo.model.Usuario

class UsuarioDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun registrar(usuario: Usuario): Long {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DBHelper.COL_NOMBRES, usuario.nombres)
            put(DBHelper.COL_CORREO, usuario.correo)
            put(DBHelper.COL_PASSWORD, usuario.password)
            put(DBHelper.COL_TELEFONO, usuario.telefono)
        }
        val res = db.insert(DBHelper.TABLE_USUARIO, null, values)
        db.close()
        return res
    }

    fun login(correo: String, pass: String): Usuario? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DBHelper.TABLE_USUARIO} WHERE ${DBHelper.COL_CORREO} = ? AND ${DBHelper.COL_PASSWORD} = ?",
            arrayOf(correo, pass)
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_USUARIO)),
                nombres = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NOMBRES)),
                correo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_CORREO)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_PASSWORD)),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TELEFONO)) ?: "",
                fechaRegistro = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_FECHA_REGISTRO))
            )
        }
        cursor.close()
        db.close()
        return usuario
    }
}