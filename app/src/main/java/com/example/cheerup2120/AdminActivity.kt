package com.example.cheerup2120

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.cheerup2120.Models.AdminAnalytics
import com.example.cheerup2120.Models.Analytics
import com.example.cheerup2120.Utils.prefs
import com.example.cheerup2120.databinding.ActivityAdminBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.common.sdkinternal.CommonUtils
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var database: DatabaseReference
    private lateinit var adminInfo: Map<String, Map<String, Map<String, Map<String, Map<String, Any>>>>>
    private var corpuseList: ArrayList<String> = ArrayList()
    private var adminAnalyticsList: ArrayList<AdminAnalytics> = ArrayList()
    private val sdf = SimpleDateFormat("dd-MM-yyyy")
    private val currentDate: String = sdf.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)

        binding.btnExitAdmin.setOnClickListener{
            prefs.admin = ""
            startActivity(Intent(this, MainActivity::class.java))
        }

        database = Firebase.database.reference
        val corpuses = arrayOf("все","ш1","ш2","ш3","ш4")
        val corpuseAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, corpuses)
        binding.actvCorpuses.setText("все")
        binding.actvCorpuses.setAdapter(corpuseAdapter)
        getAdminAnalytics("все")

        binding.actvCorpuses.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedCorpuse = parent.getItemAtPosition(position).toString()
            getAdminAnalytics(selectedCorpuse)
        }
        setContentView(binding.root)
    }

    private fun getAdminAnalytics(corpuse: String){
        var analytics = AdminAnalytics()

        database.child("Учителя").addValueEventListener(
                object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{
                            adminInfo = it.value as Map<String, Map<String, Map<String, Map<String, Map<String, Any>>>>>
                            for( (corpuse, grades) in adminInfo ){
                                corpuseList.add(corpuse)
                                for( (grade, letters) in grades ){
                                    for( (letter, uids) in letters ){
                                        for ( (uid, values) in uids ){
                                            for( userInfo in values ){
                                                if(userInfo.key == "corpuse"){
                                                    analytics.corpuse = userInfo.value as String
                                                }
                                                else if(userInfo.key == "mood"){
                                                    if(userInfo.value is Map<*, *>){
                                                        for ( (date, moodValue) in userInfo.value as Map<*, *>){
                                                            if(date == currentDate){
                                                                analytics.mood = moodValue as String
                                                                break
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            adminAnalyticsList.add(analytics)
                                            analytics = AdminAnalytics()
                                        }
                                    }
                                }
                            }
                        }
                        binding.pbAdmin.visibility = View.INVISIBLE
                        val num_bad: Float
                        val num_good: Float
                        val num_normal: Float
                        val num_not_answered: Float
                        if(corpuse == "все"){
                            corpuseList = corpuseList.distinct() as ArrayList<String>
                            num_good = adminAnalyticsList.filter{ (it.corpuse == "ш1" || it.corpuse == "ш2" || it.corpuse == "ш3" || it.corpuse == "ш4") && it.mood == "good"}.size.toFloat()
                            num_bad = adminAnalyticsList.filter{ (it.corpuse == "ш1" || it.corpuse == "ш2" || it.corpuse == "ш3" || it.corpuse == "ш4") && it.mood == "bad"}.size.toFloat()
                            num_normal = adminAnalyticsList.filter{ (it.corpuse == "ш1" || it.corpuse == "ш2" || it.corpuse == "ш3" || it.corpuse == "ш4") && it.mood == "normal"}.size.toFloat()
                            num_not_answered = adminAnalyticsList.filter{ (it.corpuse == "ш1" || it.corpuse == "ш2" || it.corpuse == "ш3" || it.corpuse == "ш4") && it.mood.isNullOrEmpty()}.size.toFloat()
                        }else {
                            num_good = adminAnalyticsList.filter { it.corpuse == corpuse && it.mood == "good" }.size.toFloat()
                            num_bad = adminAnalyticsList.filter { it.corpuse == corpuse && it.mood == "bad" }.size.toFloat()
                            num_normal = adminAnalyticsList.filter { it.corpuse == corpuse && it.mood == "normal" }.size.toFloat()
                            num_not_answered = adminAnalyticsList.filter { (it.corpuse == corpuse) && it.mood.isNullOrEmpty() }.size.toFloat()
                        }
                        setPieChartData(num_good, num_normal, num_bad, num_not_answered)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
        )
    }

    private fun setPieChartData(numGood: Float, numNeutral: Float, numBad: Float, numNotAns: Float) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(numGood, "Хорошее"))
        listColors.add(resources.getColor(R.color.goodMood))
        listPie.add(PieEntry(numBad, "Плохое"))
        listColors.add(resources.getColor(R.color.badMood))
        listPie.add(PieEntry(numNeutral, "Нормальное"))
        listColors.add(resources.getColor(R.color.neutralMood))
        listPie.add(PieEntry(numNotAns, "Не ответили"))
        listColors.add(resources.getColor(R.color.notAnsMood))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(14F)
        binding.pieChartAdmin.data = pieData

        binding.pieChartAdmin.setUsePercentValues(true)
        binding.pieChartAdmin.isDrawHoleEnabled = false
        binding.pieChartAdmin.description.isEnabled = false
        binding.pieChartAdmin.setEntryLabelColor(R.color.black)
        //chartDetails(binding.pieChartAdmin, Typeface.SANS_SERIF)
        binding.pieChartAdmin.animateY(1400, Easing.EaseInOutQuad)
    }

    fun chartDetails(mChart: PieChart, tf: Typeface) {
        mChart.description.isEnabled = true
        mChart.centerText = ""
        mChart.setCenterTextSize(10F)
        mChart.setCenterTextTypeface(tf)
        val l = mChart.legend
        mChart.legend.isWordWrapEnabled = true
        mChart.legend.isEnabled = false
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.formSize = 20F
        l.formToTextSpace = 5f
        l.form = Legend.LegendForm.SQUARE
        l.textSize = 12f
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        mChart.setTouchEnabled(false)
        mChart.setDrawEntryLabels(false)
        mChart.legend.isWordWrapEnabled = true
        mChart.setExtraOffsets(20f, 0f, 20f, 0f)
        mChart.setUsePercentValues(true)
        // mChart.rotationAngle = 0f
        mChart.setUsePercentValues(true)
        mChart.setDrawCenterText(false)
        mChart.description.isEnabled = true
        mChart.isRotationEnabled = false
    }

    override fun onBackPressed() {

    }
}