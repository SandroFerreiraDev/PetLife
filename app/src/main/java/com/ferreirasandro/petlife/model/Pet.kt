package com.ferreirasandro.petlife.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: Long = 0L,
    val name: String = "",
    val type: String = "",
    val breed: String = "",
    val age: Int = 0,
    val weight: Double = 0.0,
    val birthDate: String = ""
): Parcelable

