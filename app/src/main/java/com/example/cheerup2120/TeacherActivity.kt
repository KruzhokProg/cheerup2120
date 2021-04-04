package com.example.cheerup2120

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.cheerup2120.databinding.ActivityTeacherBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class TeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGenQr.setOnClickListener(View.OnClickListener {
            var qrText = binding.etFIOToQR.text.toString()
            val bitmap = generateQRCode(qrText)
            binding.imageView.setImageBitmap(bitmap)
        })
    }

    override fun onBackPressed() {

    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("abc", "generateQRCode: ${e.message}")
        }
        return bitmap
    }
}