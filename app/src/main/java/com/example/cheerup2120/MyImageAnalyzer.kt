package com.example.cheerup2120

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.nio.charset.Charset

class MyImageAnalyzer(private val fragmentManager: FragmentManager): ImageAnalysis.Analyzer {

    private var bottomSheet = BarcodeResultBottomSheet()

    override fun analyze(imageProxy: ImageProxy) {
        scanBarcode(imageProxy)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun scanBarcode(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()
            scanner.process(inputImage)
                .addOnCompleteListener {
                    imageProxy.close()
                    if (it.isSuccessful) {
                        readBarcodeData(it.result as List<Barcode>)
                    } else {
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

    private fun readBarcodeData(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            when (barcode.valueType) {
                //you can check if the barcode has other values
                //For now I am using it just for URL
                Barcode.TYPE_TEXT -> {
                    //we have the URL here
                    // val studentInfo = barcode.rawValue
                    if (!bottomSheet.isAdded)
                        bottomSheet.show(fragmentManager, "")
                    val tmp = barcode.rawValue!!
                    bottomSheet.updateText(tmp)
                    //barcode.rawValue?.let { bottomSheet.updateText(it) }
                    //val w1251: Charset = charset("Windows-1251")
                    //val textToDisplay = barcode.rawBytes?.let { String(it, w1251) }
//                    barcode.rawBytes?.let {
//                        if (textToDisplay != null) {
//                            bottomSheet.updateText(textToDisplay)
//                        }
//                    }
                }
            }
        }
    }


}