package com.example.cheerup2120

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cheerup2120.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        binding.viewpagerMain.adapter = fragmentAdapter

        binding.tabsMain.setupWithViewPager(binding.viewpagerMain)
    }
}