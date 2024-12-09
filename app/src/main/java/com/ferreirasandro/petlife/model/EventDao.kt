package com.ferreirasandro.petlife.model

interface EventDao {
    fun createEvent(event: Event): Long

    fun retrieveEvent(id: Long): Event

    fun retrieveEventsByPetId(petId: Long): MutableList<Event>

    fun retrieveEvents(): MutableList<Event>

    fun updateEvent(event: Event): Int

    fun deleteEvent(id: Long): Int
}
