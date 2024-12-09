package com.ferreirasandro.petlife.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

        val petTypes = listOf("Dog", "Cat")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        apb.typeSpinner.adapter = adapter

        val petSizes = listOf("Small", "Medium", "Large")
        val sizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petSizes)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        apb.sizeSpinner.adapter = sizeAdapter

        currentPet?.let { pet ->
            with(apb) {
                with(pet) {
                    nameEt.setText(name)
                    val petTypePosition = petTypes.indexOf(type)
                    typeSpinner.setSelection(petTypePosition)

                    colorEt.setText(color)

                    val petSizePosition = petSizes.indexOf(size)
                    sizeSpinner.setSelection(petSizePosition)

                    birthDateEt.setText(birthDate)

                    nameEt.isEnabled = !viewMode
                    typeSpinner.isEnabled = !viewMode
                    colorEt.isEnabled = !viewMode
                    sizeSpinner.isEnabled = !viewMode
                    birthDateEt.isEnabled = !viewMode

                    saveBt.visibility = if (viewMode) View.GONE else View.VISIBLE
                }
            }
        }

        apb.run {
            saveBt.setOnClickListener {
                currentPet?.let { pet ->
                    val selectedPetType = typeSpinner.selectedItem.toString()
                    val selectedSize = sizeSpinner.selectedItem.toString()

                    Pet(
                        id = pet.id,
                        name = nameEt.text.toString(),
                        type = selectedPetType,
                        color = colorEt.text.toString(),
                        size = selectedSize,
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
                        type = typeSpinner.selectedItem.toString(),
                        color = colorEt.text.toString(),
                        size = sizeSpinner.selectedItem.toString(),
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
