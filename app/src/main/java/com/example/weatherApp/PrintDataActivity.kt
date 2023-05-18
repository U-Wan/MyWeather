package com.example.weatherApp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherApp.databinding.ActivityPrintDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PrintDataActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPrintDataBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    lateinit var town1TextView: TextView
    lateinit var town2TextView: TextView
    lateinit var town3TextView: TextView
    var town1:String?=null
    var town2:String?=null
    var town3:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintDataBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        town1TextView = binding.txtTown1
        town2TextView = binding.txtTown2
        town3TextView = binding.txtTown3
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val userRef = database.child("users").child(currentUserUid).child("towns")
        userRef.get()
            .addOnSuccessListener { snapshot ->
                val towns = snapshot.value as Map<String, String>?
                if (towns != null) {
                    town1 = towns["town1"]
                    town2 = towns["town2"]
                    town3= towns["town3"]

                    town1TextView.text=town1.toString()
                    town2TextView.text=town2.toString()
                    town3TextView.text=town3.toString()
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Failed to retrieve user data: ${it.message}")
            }

        binding.btnshowWather.setOnClickListener{
            // Save a string to shared preferences
            val editor = sharedPreferences.edit()
            editor.putString("town_1", town1)
            editor.putString("town_2", town2)
            editor.putString("town_3", town3)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}