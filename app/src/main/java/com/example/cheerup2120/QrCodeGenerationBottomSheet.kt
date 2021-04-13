package com.example.cheerup2120

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cheerup2120.databinding.FragmentQrCodeGenerationBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.datamatrix.encoder.SymbolShapeHint
import java.util.*

class QrCodeGenerationBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentQrCodeGenerationBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQrCodeGenerationBottomSheetBinding.inflate(inflater, container, false)
        val qrText = arguments?.get("qrInfo") as String
        val studentInfo = qrText.split(",")
        val fio = studentInfo[0].trim()
        val corpuse = studentInfo[1].trim()
        val grade = studentInfo[2].trim()
        val letter = studentInfo[3].trim()
        binding.tvStudInfoBottomSheet.text = "$fio\n$corpuse\n$grade$letter"
        binding.imgvQrCode.setImageBitmap(generateQRCode(qrText))
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog;
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val hints = Hashtable<EncodeHintType, String>()
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE.toString());
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
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