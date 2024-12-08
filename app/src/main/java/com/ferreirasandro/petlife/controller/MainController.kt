package com.ferreirasandro.petlife.controller

import com.ferreirasandro.petlife.model.Pet
import com.ferreirasandro.petlife.model.PetDao
import com.ferreirasandro.petlife.model.PetSqliteImpl
import com.ferreirasandro.petlife.ui.MainActivity

class MainController(mainActivity: MainActivity) {
    private val petDao: PetDao = PetSqliteImpl(mainActivity)

    fun insertPet(pet: Pet) = petDao.createPet(pet)
    fun getPet(id: Long) = petDao.retrievePet(id)
    fun getPets() = petDao.retrievePets()
    fun modifyPet(pet: Pet) = petDao.updatePet(pet)
    fun removePet(id: Long) = petDao.deletePet(id)
}
