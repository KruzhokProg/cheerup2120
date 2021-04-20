package com.example.cheerup2120

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cheerup2120.databinding.ActivityScanQRBinding
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQRBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analyzer: MyImageAnalyzer
    private val REQUESTCODE = 101
    private val TAG = "Permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQRBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkCameraPermission()
        checkForPermissions(Manifest.permission.CAMERA, "camera", REQUESTCODE)

//        cameraExecutor = Executors.newSingleThreadExecutor()
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//            bindPreview(cameraProvider)
//        }, ContextCompat.getMainExecutor(this))
//
//        analyzer = MyImageAnalyzer(supportFragmentManager)

    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(binding.previewView.createSurfaceProvider(null))

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()


        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Intent().also {
                it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                it.data = Uri.fromParts("package", packageName, null)
                startActivity(it)
                finish()
            }
        }
    }


    private fun checkForPermissions(permission: String, name: String, requestCode: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED ->{
                    setCameraForQR()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode )
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUESTCODE)
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Требуется доступ к камере")
            setTitle("Требуется разрешение")
            setPositiveButton("Ok"){
                dialog, which ->
                ActivityCompat.requestPermissions(this@ScanQRActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUESTCODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Отказано в доступе к камере", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Permission has been denied by user")

                } else {
                    Toast.makeText(this, "Доступ к камере разрешен", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Permission has been granted by user")
                    setCameraForQR()
                }
            }
        }
    }

    private fun setCameraForQR(){
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        analyzer = MyImageAnalyzer(supportFragmentManager)
    }
}