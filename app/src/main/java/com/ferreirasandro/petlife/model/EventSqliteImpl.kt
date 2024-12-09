package com.ferreirasandro.petlife.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ferreirasandro.petlife.R

class EventSqliteImpl(context: Context) : EventDao {

    companion object {
        private const val EVENT_TABLE = "event"
        private const val ID_COLUMN = "id"
        private const val PET_ID_COLUMN = "petId"
        private const val TYPE_COLUMN = "type"
        private const val DATE_COLUMN = "date"
        private const val DESCRIPTION_COLUMN = "description"

        private const val CREATE_EVENT_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $EVENT_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$PET_ID_COLUMN INTEGER NOT NULL, " +
                    "$TYPE_COLUMN TEXT NOT NULL, " +
                    "$DATE_COLUMN TEXT NOT NULL, " +
                    "$DESCRIPTION_COLUMN TEXT NOT NULL, " +
                    "FOREIGN KEY($PET_ID_COLUMN) REFERENCES pet(id));"
    }

    private val database: SQLiteDatabase = context.openOrCreateDatabase(
        PetSqliteImpl.PET_DATABASE_FILE,
        Context.MODE_PRIVATE,
        null
    )

    init {
        try {
            //database.execSQL("DROP TABLE IF EXISTS $EVENT_TABLE")
            database.execSQL(CREATE_EVENT_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createEvent(event: Event): Long =
        database.insert(EVENT_TABLE, null, eventToContentValues(event))

    override fun retrieveEvent(id: Long): Event {
        val cursor = database.query(
            true,
            EVENT_TABLE,
            null,
            "$ID_COLUMN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) cursorToEvent(cursor) else Event()
    }

    override fun retrieveEventsByPetId(petId: Long): MutableList<Event> {
        val eventList = mutableListOf<Event>()
        val cursor = database.query(
            EVENT_TABLE,
            null,
            "$PET_ID_COLUMN = ?",
            arrayOf(petId.toString()),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            eventList.add(cursorToEvent(cursor))
        }
        return eventList
    }

    override fun retrieveEvents(): MutableList<Event> {
        val eventList = mutableListOf<Event>()
        val cursor = database.rawQuery("SELECT * FROM $EVENT_TABLE", null)
        while (cursor.moveToNext()) {
            eventList.add(cursorToEvent(cursor))
        }
        return eventList
    }

    override fun updateEvent(event: Event): Int =
        database.update(
            EVENT_TABLE,
            eventToContentValues(event),
            "$ID_COLUMN = ?",
            arrayOf(event.id.toString())
        )

    override fun deleteEvent(id: Long): Int =
        database.delete(EVENT_TABLE, "$ID_COLUMN = ?", arrayOf(id.toString()))

    private fun eventToContentValues(event: Event) = ContentValues().apply {
        with(event) {
            put(PET_ID_COLUMN, petId)
            put(TYPE_COLUMN, type.displayName)
            put(DATE_COLUMN, date)
            put(DESCRIPTION_COLUMN, description)
        }
    }

    private fun cursorToEvent(cursor: Cursor) = with(cursor) {
        val typeString = getString(getColumnIndexOrThrow(TYPE_COLUMN))
        val eventType = EventType.fromString(typeString) ?: EventType.VETERINARY_VISIT

        Event(
            id = getLong(getColumnIndexOrThrow(ID_COLUMN)),
            petId = getLong(getColumnIndexOrThrow(PET_ID_COLUMN)),
            type = eventType,
            date = getString(getColumnIndexOrThrow(DATE_COLUMN)),
            description = getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN))
        )
    }

}
