package com.example.weatherApp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.weatherApp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userEmail:String
    private lateinit var saveButton: Button
    private lateinit var town1EditText: EditText
    private lateinit var town2EditText: EditText
    private lateinit var town3EditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        saveButton=binding.btnSave
        town3EditText=binding.edtTextTown1
        town2EditText=binding.edtTextTown2
        town1EditText=binding.edtTextTown3

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        userEmail= intent.getStringExtra("usrEmail").toString()
        saveButton.setOnClickListener {
            val town1 = town1EditText.text.toString().trim()
            val town2 = town2EditText.text.toString().trim()
            val town3 = town3EditText.text.toString().trim()

            val towns = mapOf(
                "town1" to town1,
                "town2" to town2,
                "town3" to town3)

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

            val userRef = database.child("users").child(currentUserUid.toString())

            userRef.child("towns").setValue(towns)
                .addOnSuccessListener {
                    // handle success event
                    Log.d(TAG, "User data saved successfully!")
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    // handle failure event
                    Log.e(TAG, "Failed to save user data: ${it.message}")
                }
        }
    }
}
