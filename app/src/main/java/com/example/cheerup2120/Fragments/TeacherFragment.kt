package com.example.cheerup2120.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cheerup2120.NavigationTeacherActivity
import com.example.cheerup2120.TeacherActivity
import com.example.cheerup2120.Utils.prefs
import com.example.cheerup2120.databinding.FragmentTeacherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TeacherFragment : Fragment() {

    private lateinit var binding: FragmentTeacherBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTeacherBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.btnTeacherLogin.setOnClickListener {

            var email = binding.etTeacherEmail.text.toString().trim()
            val pass = binding.etTeacherPass.text.toString().trim()

            if (email.isNotEmpty() and pass.isNotEmpty()) {
                binding.pbLoadTeacherAccount.visibility = View.VISIBLE
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener {
//                    reference = Firebase.database.reference.child("Teachers").equalTo(email)
                            database = Firebase.database.reference

                            val listener = object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (!snapshot.exists()) {
                                        email = email.replace(".", "")
                                        database.child("Учителя").child(email)
                                    }
                                    prefs.teacherEmail = email
                                    prefs.myUUId = ""
                                    val intent = Intent(context, NavigationTeacherActivity::class.java)
                                    //intent.putExtra("teacherEmail", email)
                                    startActivity(intent)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                                    binding.pbLoadTeacherAccount.visibility = View.INVISIBLE
                                }
                            }

                            database.child("Учителя").equalTo(email).addValueEventListener(listener)

                        }
                        .addOnFailureListener {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            binding.pbLoadTeacherAccount.visibility = View.INVISIBLE
                        }
            }
            else{
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

            return binding.root
        }


    }
