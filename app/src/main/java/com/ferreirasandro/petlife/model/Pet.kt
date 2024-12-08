package com.ferreirasandro.petlife.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: Long = 0L,
    val name: String = "",
    val type: String = "",
    val color: String = "",
    val size: String = "",
    val birthDate: String = "",
) : Parcelable
