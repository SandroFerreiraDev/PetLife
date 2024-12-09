package com.ferreirasandro.petlife.ui

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ferreirasandro.petlife.R
import com.ferreirasandro.petlife.controller.MainController
import com.ferreirasandro.petlife.model.Event
import com.ferreirasandro.petlife.ui.EventAdapter

class EventListActivity : AppCompatActivity() {
    private lateinit var eventListView: ListView
    private val eventList: MutableList<Event> = mutableListOf()
    private lateinit var mainController: MainController
    private lateinit var eventAdapter: EventAdapter

    private var petId: Long = -1L
    private lateinit var barl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarIn)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Eventos"

        petId = intent.getLongExtra("PET_ID", -1L)

        if (petId == -1L) {
            finish()
            return
        }

        mainController = MainController(applicationContext)
        eventAdapter = EventAdapter(this, eventList)

        eventListView = findViewById(R.id.eventsLv)
        eventListView.adapter = eventAdapter

        loadEvents()

        barl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedEvent = result.data?.getParcelableExtra<Event>("EVENT")
                updatedEvent?.let { event ->
                    val position = eventList.indexOfFirst { it.id == event.id }
                    if (position == -1) {
                        if (event.id == 0L) {
                            val newId = mainController.insertEvent(event)
                            event.id = newId
                        }
                        eventList.add(event)
                    } else {
                        eventList[position] = event
                        mainController.modifyEvent(event)
                    }
                    eventAdapter.notifyDataSetChanged()
                }
            }
        }




        registerForContextMenu(eventListView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addEventMi -> {
                val intent = Intent(this, EventActivity::class.java).apply {
                    putExtra("PET_ID", petId)
                }
                barl.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_event_list, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.editEventMi -> {
                val event = eventList[position]
                val intent = Intent(this, EventActivity::class.java).apply {
                    putExtra("EVENT", event)
                    putExtra("PET_ID", petId)
                }
                barl.launch(intent)
                true
            }


            R.id.removeEventMi -> {
                val event = eventList[position]
                mainController.removeEvent(event.id)
                eventList.removeAt(position)
                eventAdapter.notifyDataSetChanged()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        loadEvents()
    }

    private fun loadEvents() {
        Thread {
            val events = mainController.getEvents(petId)
            runOnUiThread {
                eventList.clear()
                eventList.addAll(events)
                eventAdapter.notifyDataSetChanged()
            }
        }.start()
    }
}
