package com.example.cheerup2120

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cheerup2120.Fragments.AddStudentFragment
import com.example.cheerup2120.Fragments.AnalyticsFragment
import com.example.cheerup2120.databinding.ActivityNavigationTeacherBinding

class NavigationTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = resources.getString(R.string.menu_1)
        loadFragment(AddStudentFragment())
        binding.navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_qr -> {
                    title = resources.getString(R.string.menu_1)
                    loadFragment(AddStudentFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_analytics -> {
                    title = resources.getString(R.string.menu_2)
                    loadFragment(AnalyticsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.container.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}