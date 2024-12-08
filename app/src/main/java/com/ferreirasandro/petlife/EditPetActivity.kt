package com.ferreirasandro.petlife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ferreirasandro.petlife.databinding.ActivityEditPetBinding

class EditPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val type = intent.getStringExtra("type") ?: ""
        val color = intent.getStringExtra("color") ?: ""
        val size = intent.getStringExtra("size") ?: ""
        val telConsultorio = intent.getStringExtra("telConsultorio") ?: ""
        val site = intent.getStringExtra("siteConsultorio") ?: ""

        binding.etPetName.setText(name)
        binding.etBirthDate.setText(birthDate)
        binding.etType.setText(type)
        binding.etColor.setText(color)
        binding.etSize.setText(size)
        binding.etTelConsultorio.setText(telConsultorio)
        binding.etSiteConsultorio.setText(site)

        binding.btnSave.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("name", binding.etPetName.text.toString())
            resultIntent.putExtra("birthDate", binding.etBirthDate.text.toString())
            resultIntent.putExtra("type", binding.etType.text.toString())
            resultIntent.putExtra("color", binding.etColor.text.toString())
            resultIntent.putExtra("size", binding.etSize.text.toString())
            resultIntent.putExtra("telConsultorio", binding.etTelConsultorio.text.toString())
            resultIntent.putExtra("siteConsultorio", binding.etSiteConsultorio.text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}



