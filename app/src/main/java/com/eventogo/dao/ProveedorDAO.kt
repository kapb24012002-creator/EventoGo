package com.eventogo.dao

import android.content.ContentValues
import android.content.Context
import com.eventogo.database.DBHelper
import com.eventogo.model.Proveedor

class ProveedorDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertar(proveedor: Proveedor): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_ID_USUARIO, proveedor.idUsuario)
            put(DBHelper.COL_NOMBRE_PROVEEDOR, proveedor.nombre)
            put(DBHelper.COL_CORREO_PROVEEDOR, proveedor.correo)
            put(DBHelper.COL_TELEFONO_PROVEEDOR, proveedor.telefono)
            put(DBHelper.COL_DIRECCION_PROVEEDOR, proveedor.direccion)
            put(DBHelper.COL_SERVICIO, proveedor.servicio)
        }
        val res = db.insert(DBHelper.TABLE_PROVEEDOR, null, values)
        db.close()
        return res
    }

    fun obtenerTodos(): MutableList<Proveedor> {
        val lista = mutableListOf<Proveedor>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_PROVEEDOR}", null)
        if (cursor.moveToFirst()) {
            do {
                val proveedor = Proveedor(
                    idProveedor = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_PROVEEDOR)),
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_USUARIO)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NOMBRE_PROVEEDOR)),
                    correo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_CORREO_PROVEEDOR)),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TELEFONO_PROVEEDOR)),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DIRECCION_PROVEEDOR)),
                    servicio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_SERVICIO))
                )
                lista.add(proveedor)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(proveedor: Proveedor): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_NOMBRE_PROVEEDOR, proveedor.nombre)
            put(DBHelper.COL_CORREO_PROVEEDOR, proveedor.correo)
            put(DBHelper.COL_TELEFONO_PROVEEDOR, proveedor.telefono)
            put(DBHelper.COL_DIRECCION_PROVEEDOR, proveedor.direccion)
            put(DBHelper.COL_SERVICIO, proveedor.servicio)
        }
        val res = db.update(DBHelper.TABLE_PROVEEDOR, values, "${DBHelper.COL_ID_PROVEEDOR}=?", arrayOf(proveedor.idProveedor.toString()))
        db.close()
        return res
    }

    fun eliminar(idProveedor: Int): Int {
        val db = dbHelper.writableDatabase
        val res = db.delete(DBHelper.TABLE_PROVEEDOR, "${DBHelper.COL_ID_PROVEEDOR}=?", arrayOf(idProveedor.toString()))
        db.close()
        return res
    }
}