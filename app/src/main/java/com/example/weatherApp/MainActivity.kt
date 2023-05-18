package com.example.weatherApp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherApp.Fragments.FirstFragment
import com.example.weatherApp.Fragments.SecondFragment
import com.example.weatherApp.Fragments.ThirdFragment
import com.example.weatherApp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class MainActivity : AppCompatActivity(){

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    var town1:String?=null
    var town2:String?=null
    var town3:String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        town1 = sharedPreferences.getString("town_1", "Tbilisi")
        town2 = sharedPreferences.getString("town_2", "Tbilisi")
        town3 = sharedPreferences.getString("town_3", "Tbilisi")

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)



        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter.addFragment(FirstFragment(), town1!!)
        pagerAdapter.addFragment(SecondFragment(), town2.toString())
        pagerAdapter.addFragment(ThirdFragment(), town3.toString())
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pagerAdapter.getPageTitle(position)
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = it.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
