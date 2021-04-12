package com.example.cheerup2120.Fragments

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.cheerup2120.Adapters.AnalyticsAdapter
import com.example.cheerup2120.Models.Analytics
import com.example.cheerup2120.Models.Corpuse
import com.example.cheerup2120.Models.TeacherInfo
import com.example.cheerup2120.Utils.prefs
import com.example.cheerup2120.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentAnalyticsBinding
    private lateinit var database: DatabaseReference
    private lateinit var teacherInfo: Map<String, Map<String, Map<String, Any>>>
    private lateinit var studentMood: Map<String, String>
    private var corpuseList: ArrayList<String> = ArrayList()
    private var gradeList: ArrayList<String> = ArrayList()
    private var letterList: ArrayList<String> = ArrayList()
    private var studentList: ArrayList<String> = ArrayList()
    private var analyticsList: ArrayList<Analytics> = ArrayList()
//    private var corpuseAdapter: ArrayAdapter<String>? = null
//    private var classAdapter: ArrayAdapter<String>? = null
//    private var letterAdapter: ArrayAdapter<String>? = null
    private val sdf = SimpleDateFormat("dd-MM-yyyy")
    private val currentDate: String = sdf.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        //retrieve teacher's corpuses, classes, letters
        database.child("Учителя").child(prefs.teacherEmail).get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach {

//                corpuseList.clear()
//                gradeList.clear()
//                letterList.clear()
//                it.key?.let { it1 -> corpuseList.add(it1) }

                teacherInfo = it.value as Map<String, Map<String, Map<String, Any>>>
                    for ((grade, letters) in teacherInfo) {
//                        gradeList.add(grade)
                        for ((letter, ids) in letters) {
                            for (id in ids){
                                studentList.add(id.toString())
                            }
//                            letterList.add(letter)
//                            for ((id, value) in ids) {
//                                studentList.add(id)
//                            }
                        }
                    }
                }

            var analytics: Analytics
            studentList.forEach { item ->
                database.child("Учащиеся").child(item).get().addOnSuccessListener { dataSnapshot ->
                    analytics = Analytics()
                    var qwe = dataSnapshot
                    dataSnapshot.children.forEach {
                        if (it.key == "username"){
                            analytics.fio = it.value as String?
                        }
                        else if(it.key == "mood"){
                            studentMood = it.value as Map<String, String>
                            for ( (date, mood) in studentMood){
                                if(date == currentDate){
                                    analytics.mood = mood
                                    break
                                }
                            }
                        }
                    }
                    analyticsList.add(analytics)
                }
            }
            }

//        database.child("Учителя").child(prefs.teacherEmail).addValueEventListener(
//                object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        snapshot.children.forEach {
//
//                            corpuseList.clear()
//                            gradeList.clear()
//                            letterList.clear()
//
//                            teacherInfo = snapshot.value as Map<String, Map<String, Map<String, Map<String, Any>>>>
//                            for ((corpuse, grades) in teacherInfo) {
//                                corpuseList.add(corpuse)
//                                for ((grade, letters) in grades) {
//                                    gradeList.add(grade)
//                                    for ((letter, ids) in letters) {
//                                        letterList.add(letter)
//                                        for ((id, value) in ids) {
//                                            studentList.add(id)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                }
//        )

        var tmp = studentList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)

        var adapter = AnalyticsAdapter()
//        adapter.setData(studentList)
//        binding.actvCorpuses.setAdapter(corpuseAdapter)
//        binding.actvClasses.setAdapter(classAdapter)
//        binding.actvLetters.setAdapter(letterAdapter)
//
//        binding.actvCorpuses.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            binding.actvClasses.isEnabled = true
//        }

        return binding.root
    }

}



//        val entries = ArrayList<BarEntry>()
//        entries.add(BarEntry(1f, 10f))
//        entries.add(BarEntry(2f, 2f))
//        entries.add(BarEntry(3f, 7f))
//        entries.add(BarEntry(4f, 20f))
//        entries.add(BarEntry(5f, 16f))
//
//        val xAxis = arrayOf("Иванов", "Петров", "Васильев", "Сидоров", "Иртуганов")
//
//
//        val barDataset = BarDataSet(entries, "Настроение")
//        barDataset.color = Color.rgb(0, 155, 0)
//
//        binding.hBarChart.getXAxis().setValueFormatter(object : IndexAxisValueFormatter(xAxis) {
//            override fun getFormattedValue(value: Float, axis: AxisBase): String {
//                return super.getFormattedValue(value, axis)
//            }
//        })
//
//        val data = BarData(barDataset)
//        binding.hBarChart.data = data
//        binding.hBarChart.animateXY(1000, 1000)