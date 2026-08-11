package com.eventogo.dao

import android.content.ContentValues
import android.content.Context
import com.eventogo.database.DBHelper
import com.eventogo.model.Evento

class EventoDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertar(evento: Evento): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_ID_USUARIO, evento.idUsuario)
            put(DBHelper.COL_ID_CLIENTE, evento.idCliente)
            put(DBHelper.COL_TITULO, evento.titulo)
            put(DBHelper.COL_DESCRIPCION, evento.descripcion)
            put(DBHelper.COL_FECHA, evento.fecha)
            put(DBHelper.COL_HORA, evento.hora)
            put(DBHelper.COL_LUGAR, evento.lugar)
            put(DBHelper.COL_CATEGORIA, evento.categoria)
            put(DBHelper.COL_ESTADO, evento.estado)
        }
        val resultado = db.insert(DBHelper.TABLE_EVENTO, null, values)
        db.close()
        return resultado
    }

    fun obtenerTodos(): MutableList<Evento> {
        val lista = mutableListOf<Evento>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_EVENTO}", null)
        if (cursor.moveToFirst()) {
            do {
                val evento = Evento(
                    idEvento = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_EVENTO)),
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_USUARIO)),
                    idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID_CLIENTE)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITULO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESCRIPCION)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_FECHA)),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_HORA)),
                    lugar = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_LUGAR)),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_CATEGORIA)),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_ESTADO))
                )
                lista.add(evento)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(evento: Evento): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COL_ID_CLIENTE, evento.idCliente)
            put(DBHelper.COL_TITULO, evento.titulo)
            put(DBHelper.COL_DESCRIPCION, evento.descripcion)
            put(DBHelper.COL_FECHA, evento.fecha)
            put(DBHelper.COL_HORA, evento.hora)
            put(DBHelper.COL_LUGAR, evento.lugar)
            put(DBHelper.COL_CATEGORIA, evento.categoria)
            put(DBHelper.COL_ESTADO, evento.estado)
        }
        val res = db.update(DBHelper.TABLE_EVENTO, values, "${DBHelper.COL_ID_EVENTO}=?", arrayOf(evento.idEvento.toString()))
        db.close()
        return res
    }

    fun eliminar(idEvento: Int): Int {
        val db = dbHelper.writableDatabase
        val res = db.delete(DBHelper.TABLE_EVENTO, "${DBHelper.COL_ID_EVENTO}=?", arrayOf(idEvento.toString()))
        db.close()
        return res
    }
}