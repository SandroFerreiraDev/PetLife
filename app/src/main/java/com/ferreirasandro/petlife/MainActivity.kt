package com.ferreirasandro.petlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ferreirasandro.petlife.databinding.ActivityMainBinding

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
            intent.putExtra("pet", pet)
            startActivityForResult(intent, 1)
        }
    }

    private fun updateUI() {
        binding.tvPetName.text = "Nome: ${pet.name}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            pet = data?.getSerializableExtra("pet") as Pet
            updateUI()
        }
    }
}
