package com.example.cheerup2120

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cheerup2120.Utils.prefs
import com.example.cheerup2120.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(prefs.admin.isNotEmpty()){
            startActivity(Intent(this, AdminActivity::class.java))
        }
        else if(prefs.myUUId.isNotEmpty() && prefs.teacherEmail.isNotEmpty()){
            startActivity(Intent(this, StudentActivity::class.java))
        }
        else if(prefs.teacherEmail.isNotEmpty()){
            startActivity(Intent(this, NavigationTeacherActivity::class.java))
        }
        else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
            binding.viewpagerMain.adapter = fragmentAdapter

            binding.tabsMain.setupWithViewPager(binding.viewpagerMain)
        }
    }

    override fun onBackPressed() {

    }
}