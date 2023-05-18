package com.example.weatherApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherApp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySignInBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            auth = FirebaseAuth.getInstance()


            binding.signInButton.setOnClickListener {
                signInUser()
            }
            binding.txtRegister.setOnClickListener{
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }

        private fun signInUser() {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, PrintDataActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Sign in failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        }



}
