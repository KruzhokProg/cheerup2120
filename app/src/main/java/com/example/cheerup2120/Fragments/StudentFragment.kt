package com.example.cheerup2120.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.cheerup2120.MyImageAnalyzer
import com.example.cheerup2120.R
import com.example.cheerup2120.ScanQRActivity
import com.example.cheerup2120.databinding.FragmentStudentBinding
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class StudentFragment : Fragment() {

    private lateinit var binding: FragmentStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentBinding.inflate(inflater, container, false)
        binding.btnStudentSignin.setOnClickListener{
            startActivity(Intent(context, ScanQRActivity::class.java))
           //val intent = Intent(context, ScanQRActivity::class.java)
            //startActivity(intent)
        }

        return binding.root
    }
}