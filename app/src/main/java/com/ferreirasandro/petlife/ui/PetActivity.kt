package com.ferreirasandro.petlife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.ferreirasandro.petlife.R
import com.ferreirasandro.petlife.databinding.ActivityPetBinding
import com.ferreirasandro.petlife.model.Constant
import com.ferreirasandro.petlife.model.Constant.PET
import com.ferreirasandro.petlife.model.Constant.VIEW_MODE
import com.ferreirasandro.petlife.model.Pet

class PetActivity : AppCompatActivity() {
    private val apb: ActivityPetBinding by lazy {
        ActivityPetBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)

        val receivedPet = intent.getParcelableExtra<Pet>(PET)
        receivedPet?.let { pet ->
            with(apb) {
                with(pet) {
                    nameEt.setText(name)
                    typeEt.setText(type)
                    typeEt.isEnabled = false
                    birthDateEt.setText(birthDate)

                    nameEt.isEnabled = !viewMode
                    birthDateEt.isEnabled = !viewMode
                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        apb.toolbarIn.toolbar.let {
            it.subtitle = if (receivedPet == null)
                "New pet"
            else
                if (viewMode)
                    "Pet details"
                else
                    "Edit pet"
            setSupportActionBar(it)
        }

        apb.run {
            saveBt.setOnClickListener {
                Pet(
                    id = receivedPet?.id ?: 0L,
                    name = nameEt.text.toString(),
                    type = typeEt.text.toString(),
                    birthDate = birthDateEt.text.toString()
                ).let { pet ->
                    Intent().apply {
                        putExtra(Constant.PET, pet)
                        setResult(RESULT_OK, this)
                        finish()
                    }
                }
            }
        }
    }
}
