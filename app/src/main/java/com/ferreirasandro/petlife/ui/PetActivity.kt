package com.ferreirasandro.petlife.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ferreirasandro.petlife.databinding.ActivityPetBinding
import com.ferreirasandro.petlife.model.Constant.PET
import com.ferreirasandro.petlife.model.Constant.VIEW_MODE
import com.ferreirasandro.petlife.model.Pet
import com.ferreirasandro.petlife.model.PetSqliteImpl

class PetActivity : AppCompatActivity() {
    private val apb: ActivityPetBinding by lazy {
        ActivityPetBinding.inflate(layoutInflater)
    }

    private lateinit var petSqliteImpl: PetSqliteImpl
    private var currentPet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        petSqliteImpl = PetSqliteImpl(this)

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)
        currentPet = intent.getParcelableExtra(PET)

        currentPet?.let { pet ->
            with(apb) {
                with(pet) {
                    nameEt.setText(name)
                    typeEt.setText(type)
                    typeEt.isEnabled = false
                    birthDateEt.setText(birthDate)

                    nameEt.isEnabled = !viewMode
                    birthDateEt.isEnabled = !viewMode
                    saveBt.visibility = if (viewMode) View.GONE else View.VISIBLE
                }
            }
        }


        apb.run {
            saveBt.setOnClickListener {
                currentPet?.let { pet ->
                    Pet(
                        id = pet.id,
                        name = nameEt.text.toString(),
                        type = typeEt.text.toString(),
                        birthDate = birthDateEt.text.toString()
                    ).let { updatedPet ->
                        petSqliteImpl.updatePet(updatedPet)
                        Toast.makeText(this@PetActivity, "Pet updated", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                } ?: run {
                    val newPet = Pet(
                        id = 0L,
                        name = nameEt.text.toString(),
                        type = typeEt.text.toString(),
                        birthDate = birthDateEt.text.toString()
                    )
                    petSqliteImpl.createPet(newPet)
                    Toast.makeText(this@PetActivity, "Pet added", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }

        }
    }
}
