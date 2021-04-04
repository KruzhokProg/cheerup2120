package com.example.cheerup2120

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cheerup2120.Fragments.StudentFragment
import com.example.cheerup2120.Fragments.TeacherFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
       return when (position){
           0 -> TeacherFragment()
           1 -> StudentFragment()
           else -> TeacherFragment()
       }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Учитель"
            1 -> "Ученик"
            else -> ""
        }
    }
}