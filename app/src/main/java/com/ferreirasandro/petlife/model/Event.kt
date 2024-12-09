package com.ferreirasandro.petlife.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    var id: Long = 0L,
    val petId: Long = 0L,
    val type: EventType = EventType.VETERINARY_VISIT,
    val date: String = "",
    val description: String = "",
    val time: String = "",
) : Parcelable {

}
