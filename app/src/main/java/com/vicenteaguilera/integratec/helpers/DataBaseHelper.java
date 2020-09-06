package com.vicenteaguilera.integratec.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;

public class DataBaseHelper extends SQLiteOpenHelper
{
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table asesorados(id integer primary key, nControl integer, nombre text,semestre text, carrera text, materia text, tema text, fecha text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("drop table if exists "+ PropiertiesHelper.NOMBRE_TABLA);
        onCreate(db);
    }
}
