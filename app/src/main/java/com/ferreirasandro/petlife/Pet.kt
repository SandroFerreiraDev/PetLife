package com.ferreirasandro.petlife
import java.io.Serializable

data class Pet(
    var name: String,
    var birthDate: String,
    var type: String,
    var color: String,
    var size: String,
    var lastVetVisit: String,
    var lastVaccination: String,
    var lastPetShopVisit: String
)

