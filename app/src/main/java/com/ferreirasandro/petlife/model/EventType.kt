package com.ferreirasandro.petlife.model

enum class EventType(val displayName: String) {
    VETERINARY_VISIT("Ida ao Veterinário"),
    VACCINATION("Vacinação"),
    PETSHOP_VISIT("Ida ao Petshop"),
    REMEDY("Medicação");

    companion object {
        fun fromString(displayName: String): EventType? {
            return values().find { it.displayName == displayName }
        }
    }
}

