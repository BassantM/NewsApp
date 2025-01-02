package com.example.lec29

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.lec29.databinding.ActivityLoginBinding
import com.example.lec29.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        binding.loginBtn.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val pass = binding.passwordEt.text.toString()

            if(email.isBlank() || pass.isBlank())
                Toast.makeText(this, "Missing Filed/s ", Toast.LENGTH_LONG).show()
            else {
                binding.progress.isVisible=true
                login(email,pass)
            }

        }
        binding.notSignedTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        binding.forgetPassTv.setOnClickListener {
            val email = binding.emailEt.text.toString()
            binding.progress.isVisible=true
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                       binding.progress.isVisible=false
                        Toast.makeText(this, "Email sent ", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified)
                    {
                       startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    else
                        Toast.makeText(this, "Check your Email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "${task.exception?.message}",Toast.LENGTH_LONG).show()
                    binding.progress.isVisible=false
                }
            }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}