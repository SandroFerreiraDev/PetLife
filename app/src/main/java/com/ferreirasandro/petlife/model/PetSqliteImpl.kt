package com.ferreirasandro.petlife.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ferreirasandro.petlife.R

class PetSqliteImpl(context: Context) : PetDao {

    companion object {
        private const val PET_DATABASE_FILE = "petlife"
        private const val PET_TABLE = "pet"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val TYPE_COLUMN = "type"
        private const val COLOR_COLUMN = "color"
        private const val SIZE_COLUMN = "size"
        private const val BIRTHDATE_COLUMN = "birthDate"

        private const val CREATE_PET_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $PET_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$TYPE_COLUMN TEXT NOT NULL, " +
                    "$COLOR_COLUMN TEXT NOT NULL, " +
                    "$SIZE_COLUMN TEXT NOT NULL, " +
                    "$BIRTHDATE_COLUMN TEXT NOT NULL);"
    }

    private val petDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        PET_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    init {
        try {
            //petDatabase.execSQL("DROP TABLE IF EXISTS $PET_TABLE")

            petDatabase.execSQL(CREATE_PET_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createPet(pet: Pet) =
        petDatabase.insert(PET_TABLE, null, petToContentValues(pet))

    override fun retrievePet(id: Long): Pet {
        val cursor = petDatabase.query(
            true,
            PET_TABLE,
            null,
            "$ID_COLUMN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursorToPet(cursor)
        } else {
            Pet()
        }
    }

    override fun retrievePets(): MutableList<Pet> {
        val petList = mutableListOf<Pet>()

        val cursor = petDatabase.rawQuery("SELECT * FROM $PET_TABLE", null)
        while (cursor.moveToNext()) {
            petList.add(cursorToPet(cursor))
        }

        return petList
    }

    override fun updatePet(pet: Pet) = petDatabase.update(
        PET_TABLE,
        petToContentValues(pet),
        "$ID_COLUMN = ?",
        arrayOf(pet.id.toString())
    )

    override fun deletePet(id: Long) = petDatabase.delete(
        PET_TABLE,
        "$ID_COLUMN = ?",
        arrayOf(id.toString())
    )

    private fun petToContentValues(pet: Pet) = ContentValues().apply {
        with(pet) {
            put(NAME_COLUMN, name)
            put(TYPE_COLUMN, type)
            put(COLOR_COLUMN, color)
            put(SIZE_COLUMN, size)
            put(BIRTHDATE_COLUMN, birthDate)  // Salvando o birthDate
        }
    }

    private fun cursorToPet(cursor: Cursor) = with(cursor) {
        Pet(
            id = getLong(getColumnIndexOrThrow(ID_COLUMN)),
            name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
            type = getString(getColumnIndexOrThrow(TYPE_COLUMN)),
            color = getString(getColumnIndexOrThrow(COLOR_COLUMN)),
            size = getString(getColumnIndexOrThrow(SIZE_COLUMN)),
            birthDate = getString(getColumnIndexOrThrow(BIRTHDATE_COLUMN))  // Recuperando o birthDate
        )
    }
}
