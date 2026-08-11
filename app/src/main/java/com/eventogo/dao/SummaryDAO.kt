package com.eventogo.dao

import android.content.Context
import com.eventogo.database.DBHelper

class SummaryDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun getCounts(): Map<String, Int> {
        val counts = mutableMapOf<String, Int>()
        val db = dbHelper.readableDatabase

        val cursorEventos = db.rawQuery("SELECT COUNT(*) FROM ${DBHelper.TABLE_EVENTO}", null)
        if (cursorEventos.moveToFirst()) counts["eventos"] = cursorEventos.getInt(0)
        cursorEventos.close()

        val cursorClientes = db.rawQuery("SELECT COUNT(*) FROM ${DBHelper.TABLE_CLIENTE}", null)
        if (cursorClientes.moveToFirst()) counts["clientes"] = cursorClientes.getInt(0)
        cursorClientes.close()

        val cursorProveedores = db.rawQuery("SELECT COUNT(*) FROM ${DBHelper.TABLE_PROVEEDOR}", null)
        if (cursorProveedores.moveToFirst()) counts["proveedores"] = cursorProveedores.getInt(0)
        cursorProveedores.close()

        db.close()
        return counts
    }
}