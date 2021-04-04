package com.example.cheerup2120

import android.annotation.SuppressLint
import android.view.textclassifier.TextClassifier.TYPE_URL
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

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
                    barcode.rawValue?.let { bottomSheet.updateText(it) }
                }
            }
        }
    }


}