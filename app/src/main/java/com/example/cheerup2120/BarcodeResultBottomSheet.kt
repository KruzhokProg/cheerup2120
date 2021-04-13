package com.example.cheerup2120

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cheerup2120.Utils.prefs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class BarcodeResultBottomSheet: BottomSheetDialogFragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_qr_data, container, false)

    fun updateText(text: String) {
        view?.apply {

            val studentInfo = text.split(", ")
            val fio = studentInfo[0]
            val corpuse = studentInfo[1]
            val grade = studentInfo[2]
            val letter = studentInfo[3]
            val teacherEmail = studentInfo[4]
            val student = Student(fio, corpuse, grade, letter, "")

            findViewById<TextView>(R.id.tvStudentQRFIO)?.text = fio
            findViewById<TextView>(R.id.tvStudentQRCorpusClass)?.text = corpuse + " " + grade + letter
            findViewById<Button>(R.id.btnOk).setOnClickListener{
                //startActivity(Intent(it.context, StudentActivity::class.java).also { it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)}
                //        .also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)})
                val uuid = UUID.randomUUID().toString()
                database = Firebase.database.reference
//                database.child("Учащиеся").child(uuid).setValue(student)
                database.child("Учителя").child(teacherEmail).child(corpuse).child(grade).child(letter).child(uuid)
                        .setValue(student)

                prefs.myUUId = uuid
                prefs.studentFIO = fio
                prefs.studentClass = grade
                prefs.studentClassLetter = letter
                prefs.studentCorpus = corpuse
//                prefs.studentClass = "$grade$letter     $corpuse"
                prefs.teacherEmail = teacherEmail
                val intent = Intent(it.context, StudentActivity::class.java)
                startActivity(intent)
            }
        }
    }

}