package com.ferreirasandro.petlife.ui

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ferreirasandro.petlife.R
import com.ferreirasandro.petlife.databinding.TilePetBinding
import com.ferreirasandro.petlife.model.Pet

class PetAdapter(
    context: Context,
    private val petList: MutableList<Pet>
) : ArrayAdapter<Pet>(context, R.layout.tile_pet, petList) {

    private data class PetTileHolder(
        val nameTv: TextView,
        val typeTv: TextView,
        val birthDateTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var tpb: TilePetBinding

        val pet = petList[position]

        var petTile = convertView
        if (petTile == null) {
            tpb = TilePetBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            petTile = tpb.root

            val newPetTileHolder = PetTileHolder(tpb.petNameTv, tpb.petAgeTv, tpb.petLastVisitTv)

            petTile.tag = newPetTileHolder
        }

        val holder = petTile.tag as PetTileHolder
        holder.let {
            it.nameTv.text = pet.name
            it.typeTv.text = pet.type
            it.birthDateTv.text = pet.birthDate
        }

        return petTile
    }
}
