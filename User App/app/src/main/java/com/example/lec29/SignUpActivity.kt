package com.example.lec29

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.lec29.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

        binding.signUpBtn.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val pass = binding.passwordEt.text.toString()
            val confirmPass = binding.confirmPasswordEt.text.toString()

            if(email.isBlank() || pass.isBlank() || confirmPass.isBlank())
                Toast.makeText(this, "Missing Filed/s ",Toast.LENGTH_LONG).show()
            else if(pass.length<6)
                Toast.makeText(this, "Password is too short", Toast.LENGTH_LONG).show()
            else if (pass !=confirmPass)
                Toast.makeText(this, "Password not matched with confirm pass", Toast.LENGTH_LONG).show()
            else {
                binding.progress.isVisible=true
                createUser(email,pass)
            }

        }
        binding.alreadyUserTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }

    private fun verfiyEmail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email, Please.",Toast.LENGTH_LONG).show()
                    binding.progress.isVisible=false
                }
            }
    }

    private fun createUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verfiyEmail()
                } else {
                    Toast.makeText(this, "${task.exception?.message}",Toast.LENGTH_LONG).show()
                    binding.progress.isVisible=false
                }
            }
    }
}