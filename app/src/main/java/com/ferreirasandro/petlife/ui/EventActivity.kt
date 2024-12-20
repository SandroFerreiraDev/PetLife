package com.ferreirasandro.petlife.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ferreirasandro.petlife.databinding.ActivityEventBinding
import com.ferreirasandro.petlife.model.Constant.EVENT
import com.ferreirasandro.petlife.model.Constant.VIEW_MODE
import com.ferreirasandro.petlife.model.Event
import com.ferreirasandro.petlife.model.EventSqliteImpl
import com.ferreirasandro.petlife.model.EventType

class EventActivity : AppCompatActivity() {
    private val aeb: ActivityEventBinding by lazy {
        ActivityEventBinding.inflate(layoutInflater)
    }

    private lateinit var eventSqliteImpl: EventSqliteImpl
    private var currentEvent: Event? = null
    private var petId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aeb.root)

        eventSqliteImpl = EventSqliteImpl(this)

        petId = intent.getLongExtra("PET_ID", -1L)

        if (petId == -1L) {
            Toast.makeText(this, "Pet ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)
        currentEvent = intent.getParcelableExtra(EVENT)

        val eventTypes = EventType.values().map { it.displayName }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        aeb.typeSpinner.adapter = spinnerAdapter

        currentEvent?.let { event ->
            val position = eventTypes.indexOf(event.type.displayName)
            aeb.typeSpinner.setSelection(position)

            with(aeb) {
                dateEt.setText(event.date)
                descriptionEt.setText(event.description)


                if (event.type == EventType.REMEDY) {
                    timeEt.setText(event.time)
                    timeEt.visibility = View.VISIBLE
                } else {
                    timeEt.visibility = View.GONE
                }

                typeSpinner.isEnabled = !viewMode
                dateEt.isEnabled = !viewMode
                descriptionEt.isEnabled = !viewMode
                timeEt.isEnabled = !viewMode

                saveBt.visibility = if (viewMode) View.GONE else View.VISIBLE
            }
        }

        aeb.typeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = EventType.values()[position]
                aeb.timeEt.visibility = if (selectedType == EventType.REMEDY) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
            }
        }


        aeb.saveBt.setOnClickListener {
            val selectedType = EventType.fromString(aeb.typeSpinner.selectedItem.toString()) ?: EventType.VETERINARY_VISIT

            val updatedEvent = Event(
                id = currentEvent?.id ?: 0L,
                petId = petId,
                type = selectedType,
                date = aeb.dateEt.text.toString(),
                description = aeb.descriptionEt.text.toString(),
                time = aeb.timeEt.text.toString()
            )

            if (currentEvent != null) {
                eventSqliteImpl.updateEvent(updatedEvent)
                Toast.makeText(this@EventActivity, "Event updated", Toast.LENGTH_SHORT).show()
            } else {
                eventSqliteImpl.createEvent(updatedEvent)
                Toast.makeText(this@EventActivity, "Event added", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }
    }
}
