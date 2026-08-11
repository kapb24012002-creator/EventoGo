package com.eventogo.dao

import android.content.ContentValues
import android.content.Context
import com.eventogo.database.DBHelper
import com.eventogo.model.Pago

class PagoDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertar(pago: Pago): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_PAGO_CLIENTE, pago.idCliente)
            put(DBHelper.COL_MONTO, pago.monto)
            put(DBHelper.COL_FECHA_PAGO, pago.fechaPago)
            put(DBHelper.COL_METODO_PAGO, pago.metodoPago)
            put(DBHelper.COL_ESTADO_PAGO, pago.estadoPago)
        }
        val res = db.insert(DBHelper.TABLE_PAGO, null, values)
        db.close()
        return res
    }

    fun obtenerTodos(): MutableList<Pago> {
        val lista = mutableListOf<Pago>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_PAGO}", null)
        if (cursor.moveToFirst()) {
            do {
                val pago = Pago(
                    idPago = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_PAGO)),
                    idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_PAGO_CLIENTE)),
                    monto = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COL_MONTO)),
                    fechaPago = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_FECHA_PAGO)),
                    metodoPago = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_METODO_PAGO)),
                    estadoPago = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_ESTADO_PAGO))
                )
                lista.add(pago)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(pago: Pago): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_PAGO_CLIENTE, pago.idCliente)
            put(DBHelper.COL_MONTO, pago.monto)
            put(DBHelper.COL_FECHA_PAGO, pago.fechaPago)
            put(DBHelper.COL_METODO_PAGO, pago.metodoPago)
            put(DBHelper.COL_ESTADO_PAGO, pago.estadoPago)
        }
        val res = db.update(DBHelper.TABLE_PAGO, values, "${DBHelper.COL_ID_PAGO}=?", arrayOf(pago.idPago.toString()))
        db.close()
        return res
    }

    fun eliminar(idPago: Int): Int {
        val db = dbHelper.writableDatabase
        val res = db.delete(DBHelper.TABLE_PAGO, "${DBHelper.COL_ID_PAGO}=?", arrayOf(idPago.toString()))
        db.close()
        return res
    }
}