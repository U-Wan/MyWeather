package com.example.weatherApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Patterns
import com.example.weatherApp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


class SignUpActivity : AppCompatActivity() {

    private lateinit var userEmail:String
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySignUpBinding.inflate(layoutInflater)
            setContentView(binding.root)


            auth = FirebaseAuth.getInstance()
            binding.txtSignIn.setOnClickListener{
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }

            binding.buttonSignUp.setOnClickListener {
                val name = binding.editTextName.text.toString().trim()
                val email = binding.editTextEmail.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                val repeatPassword = binding.editTextRepeatPassword.text.toString().trim()
                userEmail=email

                if (name.isEmpty()) {
                    binding.editTextName.error = "Name is required"
                    binding.editTextName.requestFocus()
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    binding.editTextEmail.error = "Email is required"
                    binding.editTextEmail.requestFocus()
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.editTextEmail.error = "Invalid email format"
                    binding.editTextEmail.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    binding.editTextPassword.error = "Password is required"
                    binding.editTextPassword.requestFocus()
                    return@setOnClickListener
                }
                if (password != repeatPassword) {
                    binding.editTextRepeatPassword.error = "Passwords do not match"
                    binding.editTextRepeatPassword.requestFocus()
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateProfileName(user, name)
                            Toast.makeText(
                                this@SignUpActivity,
                                "Sign up successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            if (task.exception is FirebaseAuthUserCollisionException) {
                                binding.editTextEmail.error = "Email already in use"
                                binding.editTextEmail.requestFocus()
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Sign up failed. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("usrEmail", userEmail)
                startActivity(intent)
            }
        }

        private fun updateProfileName(user: FirebaseUser?, name: String) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user?.updateProfile(profileUpdates)
        }
    }
