package com.ferreirasandro.petlife.controller

import android.content.Context
import com.ferreirasandro.petlife.model.Event
import com.ferreirasandro.petlife.model.EventDao
import com.ferreirasandro.petlife.model.EventSqliteImpl
import com.ferreirasandro.petlife.model.Pet
import com.ferreirasandro.petlife.model.PetDao
import com.ferreirasandro.petlife.model.PetSqliteImpl

class MainController(context: Context) {

    private val petDao: PetDao = PetSqliteImpl(context)
    private val eventDao: EventDao = EventSqliteImpl(context)

    fun insertPet(pet: Pet) = petDao.createPet(pet)
    fun getPet(id: Long) = petDao.retrievePet(id)
    fun getPets() = petDao.retrievePets()
    fun modifyPet(pet: Pet) = petDao.updatePet(pet)
    fun removePet(id: Long) = petDao.deletePet(id)

    fun insertEvent(event: Event) = eventDao.createEvent(event)
    fun getEvent(id: Long) = eventDao.retrieveEvent(id)
    fun getEvents(petId: Long) = eventDao.retrieveEventsByPetId(petId)
    fun modifyEvent(event: Event) = eventDao.updateEvent(event)
    fun removeEvent(id: Long) = eventDao.deleteEvent(id)
}
