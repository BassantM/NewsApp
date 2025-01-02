package com.example.lec29

import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lec29.databinding.ActivityCountryBinding

class CountryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityCountryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val group : RadioGroup= findViewById(R.id.radio_group)
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId)
            {
                R.id.us_rb->changeCountry("us")
                R.id.de_rb->changeCountry("de")


            }
        }
    }
    fun changeCountry(countryCode:String)
    {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE).edit()
        prefs.putString("code",countryCode)
        prefs.apply()
    }
}