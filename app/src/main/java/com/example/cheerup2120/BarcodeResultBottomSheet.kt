package com.example.cheerup2120

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BarcodeResultBottomSheet: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_qr_data, container, false)

    fun updateText(text: String) {
        view?.apply {
            findViewById<TextView>(R.id.tvStudentQRFIO)?.text = text
            findViewById<Button>(R.id.btnOk).setOnClickListener{
                //startActivity(Intent(it.context, StudentActivity::class.java).also { it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)}
                //        .also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)})


                val intent = Intent(it.context, StudentActivity::class.java)
                startActivity(intent)
            }
            findViewById<Button>(R.id.btnCancel).setOnClickListener{
                Toast.makeText(context, " Тогда ничего не получится :((", Toast.LENGTH_SHORT).show()
            }
        }
    }

}