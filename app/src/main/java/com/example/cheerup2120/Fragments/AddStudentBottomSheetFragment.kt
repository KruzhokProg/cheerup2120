package com.example.cheerup2120.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.cheerup2120.BarcodeResultBottomSheet
import com.example.cheerup2120.MainActivity
import com.example.cheerup2120.QrCodeGenerationBottomSheet
import com.example.cheerup2120.R
import com.example.cheerup2120.Utils.prefs
import com.example.cheerup2120.databinding.FragmentAddStudentBinding
import com.example.cheerup2120.databinding.FragmentAddStudentBottomSheetBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.datamatrix.encoder.SymbolShapeHint
import java.util.*

class AddStudentBottomSheetFragment : Fragment() {

    private lateinit var binding: FragmentAddStudentBottomSheetBinding
    private var selectedCorpuse: String? = null
    private var selectedClass: String? = null
    private var selectedLetter: String? = null
    private var isFioValid = false
    private var bottomSheet = QrCodeGenerationBottomSheet()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentAddStudentBottomSheetBinding.inflate(inflater, container, false)

        binding.btnExitTeacher.setOnClickListener{
            prefs.teacherEmail=""
            prefs.myUUId=""
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        val corpuses = arrayOf("ш1","ш2","ш3","ш4")
        val classes = arrayOf("1","2","3","4","5","6","7","8","9","10","11")
        val letters = arrayOf("а", "б", "в", "г", "д", "е", "ж", "з", "и", "к", "л", "м", "н", "о", "п")
        val corpuseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, corpuses)
        val classAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, classes)
        val letterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, letters)
        binding.actvCorpuses.setAdapter(corpuseAdapter)
        binding.actvClasses.setAdapter(classAdapter)
        binding.actvLetters.setAdapter(letterAdapter)

        binding.actvCorpuses.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectedCorpuse = parent.getItemAtPosition(position).toString()
        }
        binding.actvClasses.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectedClass = parent.getItemAtPosition(position).toString()
        }
        binding.actvLetters.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectedLetter = parent.getItemAtPosition(position).toString()
        }

        binding.etFIOToQR.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val fio = s.toString().split(" ")
                if( (fio.size == 3 && fio[0].isNotEmpty() && fio[1].isNotEmpty() && fio[2].isNotEmpty()) ||
                    (fio.size == 2 && fio[0].isNotEmpty() && fio[1].isNotEmpty()) ){
                    binding.textInputFIOToQR.error = null
                    isFioValid = true
                }
                else{
                    binding.textInputFIOToQR.error = "Некорректный ввод: уажите фамилию, имя и отчество(если есть) через пробел"
                    isFioValid = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnGenQr.setOnClickListener(View.OnClickListener {

            if (isFioValid && selectedCorpuse != null && selectedClass != null && selectedLetter != null) {
                val fio = binding.etFIOToQR.text.toString()
                val teacherEmail = prefs.teacherEmail
                val qrText = "$fio, $selectedCorpuse, $selectedClass, $selectedLetter, $teacherEmail"
                val bitmap = generateQRCode(qrText)
//                binding.imageView.setImageBitmap(bitmap)
                if (!bottomSheet.isAdded) {
                    val bundle = Bundle()
                    bundle.putString("qrInfo", qrText)
                    bottomSheet.arguments = bundle
                    bottomSheet.show(childFragmentManager, "")
                }

            }
            else{
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun generateQRCode(text: String): Bitmap {
        TransitionManager.beginDelayedTransition(binding.transitionContainer)
        binding.imageView.visibility = View.VISIBLE
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