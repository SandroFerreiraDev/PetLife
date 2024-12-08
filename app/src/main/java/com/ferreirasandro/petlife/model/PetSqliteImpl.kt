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
        private const val BREED_COLUMN = "breed"
        private const val AGE_COLUMN = "age"
        private const val WEIGHT_COLUMN = "weight"

        private const val CREATE_PET_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $PET_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$TYPE_COLUMN TEXT NOT NULL, " +
                    "$BREED_COLUMN TEXT NOT NULL, " +
                    "$AGE_COLUMN INTEGER NOT NULL, " +
                    "$WEIGHT_COLUMN REAL NOT NULL);"
    }

    private val petDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        PET_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    init {
        try {
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
            put(BREED_COLUMN, breed)
            put(AGE_COLUMN, age)
            put(WEIGHT_COLUMN, weight)
        }
    }

    private fun cursorToPet(cursor: Cursor) = with(cursor) {
        Pet(
            id = getLong(getColumnIndexOrThrow(ID_COLUMN)),
            name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
            type = getString(getColumnIndexOrThrow(TYPE_COLUMN)),
            breed = getString(getColumnIndexOrThrow(BREED_COLUMN)),
            age = getInt(getColumnIndexOrThrow(AGE_COLUMN)),
            weight = getDouble(getColumnIndexOrThrow(WEIGHT_COLUMN))
        )
    }
}
