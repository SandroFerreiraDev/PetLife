package com.ferreirasandro.petlife

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.ferreirasandro.petlife.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var pet: Pet
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pet = Pet(
            name = "Rex",
            birthDate = "01/01/2020",
            type = "Cão",
            color = "Marrom",
            size = "Médio",
            lastVetVisit = "01/09/2024",
            lastVaccination = "15/08/2024",
            lastPetShopVisit = "10/08/2024"
        )

        updateUI()

        binding.btnEditPet.setOnClickListener {
            val intent = Intent(this, EditPetActivity::class.java)
            intent.putExtra("name", pet.name)
            intent.putExtra("birthDate", pet.birthDate)
            intent.putExtra("type", pet.type)
            intent.putExtra("color", pet.color)
            intent.putExtra("size", pet.size)
            startActivityForResult(intent, 1)
        }

        binding.btnChangeVetVisitDate.setOnClickListener {
            showDatePicker { date ->
                pet.lastVetVisit = date
                updateUI()
            }
        }

        binding.btnChangeVaccinationDate.setOnClickListener {
            showDatePicker { date ->
                pet.lastVaccination = date
                updateUI()
            }
        }

        binding.btnChangePetShopVisitDate.setOnClickListener {
            showDatePicker { date ->
                pet.lastPetShopVisit = date
                updateUI()
            }
        }
    }


    private fun updateUI() {
        binding.tvPetName.text = "Nome: ${pet.name}"
        binding.tvBirthDate.text = "Data de Nascimento: ${pet.birthDate}"
        binding.tvType.text = "Tipo: ${pet.type}"
        binding.tvColor.text = "Cor: ${pet.color}"
        binding.tvSize.text = "Porte: ${pet.size}"
        binding.tvLastVetVisit.text = "Última Visita ao Veterinário: ${pet.lastVetVisit}"
        binding.tvLastVaccination.text = "Última Vacinação: ${pet.lastVaccination}"
        binding.tvLastPetShopVisit.text = "Última Visita ao Petshop: ${pet.lastPetShopVisit}"
    }

    private fun showDatePicker(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSet(date)
        }, year, month, day).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            pet.name = data.getStringExtra("name") ?: pet.name
            pet.birthDate = data.getStringExtra("birthDate") ?: pet.birthDate
            pet.type = data.getStringExtra("type") ?: pet.type
            pet.color = data.getStringExtra("color") ?: pet.color
            pet.size = data.getStringExtra("size") ?: pet.size

            updateUI()
        }
    }
}
