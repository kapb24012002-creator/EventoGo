package com.eventogo.dao

import android.content.ContentValues
import android.content.Context
import com.eventogo.database.DBHelper
import com.eventogo.model.Cliente

class ClienteDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertar(cliente: Cliente): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_ID_USUARIO, cliente.idUsuario)
            put(DBHelper.COL_NOMBRE_CLIENTE, cliente.nombre)
            put(DBHelper.COL_CORREO_CLIENTE, cliente.correo)
            put(DBHelper.COL_TELEFONO_CLIENTE, cliente.telefono)
            put(DBHelper.COL_DIRECCION_CLIENTE, cliente.direccion)
            put(DBHelper.COL_TIPO_CLIENTE, cliente.tipoCliente)
        }
        val res = db.insert(DBHelper.TABLE_CLIENTE, null, values)
        db.close()
        return res
    }

    fun obtenerTodos(): MutableList<Cliente> {
        val lista = mutableListOf<Cliente>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_CLIENTE}", null)
        if (cursor.moveToFirst()) {
            do {
                val cliente = Cliente(
                    idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_CLIENTE)),
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_USUARIO)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NOMBRE_CLIENTE)),
                    correo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_CORREO_CLIENTE)),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TELEFONO_CLIENTE)),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DIRECCION_CLIENTE)),
                    tipoCliente = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TIPO_CLIENTE))
                )
                lista.add(cliente)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(cliente: Cliente): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_NOMBRE_CLIENTE, cliente.nombre)
            put(DBHelper.COL_CORREO_CLIENTE, cliente.correo)
            put(DBHelper.COL_TELEFONO_CLIENTE, cliente.telefono)
            put(DBHelper.COL_DIRECCION_CLIENTE, cliente.direccion)
            put(DBHelper.COL_TIPO_CLIENTE, cliente.tipoCliente)
        }
        val res = db.update(DBHelper.TABLE_CLIENTE, values, "${DBHelper.COL_ID_CLIENTE}=?", arrayOf(cliente.idCliente.toString()))
        db.close()
        return res
    }

    fun eliminar(idCliente: Int): Int {
        val db = dbHelper.writableDatabase
        val res = db.delete(DBHelper.TABLE_CLIENTE, "${DBHelper.COL_ID_CLIENTE}=?", arrayOf(idCliente.toString()))
        db.close()
        return res
    }
}