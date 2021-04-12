package com.example.cheerup2120

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.cheerup2120.Utils.prefs
import com.example.cheerup2120.databinding.ActivityStudentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.text.SimpleDateFormat
import java.util.*

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding
    private lateinit var database: DatabaseReference
    private val sdf = SimpleDateFormat("dd-MM-yyyy")
    private val currentDate: String = sdf.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        checkTodayVote2()
        setSupportActionBar(binding.toolbarStudent)
        binding.tvStudFIO.text = prefs.studentFIO
        binding.tvStudClassInfo.text = prefs.studentClass

    }


    override fun onBackPressed() {
    }

//    private fun scanQRCode() {
//        val integrator = IntentIntegrator(this).apply {
//            captureActivity = CaptureActivity::class.java
//            setOrientationLocked(false)
//            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//            setPrompt("Scanning Code")
//        }
//        integrator.initiateScan()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
//            else Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }

    fun imgvGoodMood_Click(view: View) {
        database.child("Учащиеся").child(prefs.myUUId).child("mood").child(currentDate).setValue("good")
        binding.imgvGoodMood.zoomOut(1500)
                .mergeWith(binding.imgvNeutralMood.zoomIn(1500))
                .mergeWith(binding.imgvBadMood.zoomIn(1500))
                .subscribe()
    }

    fun imgvNeutralMood_Click(view: View) {
        database.child("Учащиеся").child(prefs.myUUId).child("mood").child(currentDate).setValue("normal")
        binding.imgvNeutralMood.zoomOut(1500)
                .mergeWith(binding.imgvGoodMood.zoomIn(1500))
                .mergeWith(binding.imgvBadMood.zoomIn(1500))
                .subscribe()
    }

    fun imgvBadMood_Click(view: View) {
        database.child("Учащиеся").child(prefs.myUUId).child("mood").child(currentDate).setValue("bad")
        binding.imgvBadMood.zoomOut(1500)
                .mergeWith(binding.imgvGoodMood.zoomIn(1500))
                .mergeWith(binding.imgvNeutralMood.zoomIn(1500))
                .subscribe()
    }

    fun exit_student_account(view: View) {
        prefs.myUUId = ""
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun voteItemVisibility(mode: Int, comment: String){
        binding.imgvGoodMood.visibility = mode
        binding.imgvBadMood.visibility = mode
        binding.imgvNeutralMood.visibility = mode
        binding.tvMoodComment.visibility = View.VISIBLE
        binding.pbLoadStudentVoteState.visibility = View.INVISIBLE

        when(mode){
            View.VISIBLE -> binding.imgvVoteDone.visibility = View.INVISIBLE
            View.INVISIBLE -> binding.imgvVoteDone.visibility = View.VISIBLE
        }
        binding.tvMoodComment.text = comment

    }

    fun checkTodayVote2(){
        database.child("Учащиеся").child(prefs.myUUId).child("mood").get().addOnSuccessListener {
            var exist = false
            var moodValue: String = ""
            it.children.forEach {
                if(it.key == currentDate){
                    exist = true
                    moodValue = it.value as String
                    return@forEach
                }
            }

            if (exist){
                binding.imgvVoteDone.alpha = 0f
                binding.imgvVoteDone.scaleX = 0f
                binding.imgvVoteDone.scaleY = 0f
                binding.imgvAnsDone.alpha = 0f
                binding.imgvAnsDone.scaleX = 0f
                binding.imgvAnsDone.scaleY = 0f
                binding.imgvVoteDone.visibility = View.VISIBLE
                binding.imgvAnsDone.visibility = View.VISIBLE

                when(moodValue){
                    "bad" -> binding.imgvAnsDone.setImageResource(R.drawable.ic_thumb_bad)
                    "normal" -> binding.imgvAnsDone.setImageResource(R.drawable.ic_thumb_neutral)
                     else -> binding.imgvAnsDone.setImageResource(R.drawable.ic_thumb_good)
                }


                binding.imgvVoteDone.fadeIn(1000).subscribe()
                binding.imgvAnsDone.fadeIn(1000).subscribe()
                voteItemVisibility(View.INVISIBLE, "Ответ дан!")
            }
            else{
                binding.imgvVoteDone.visibility = View.INVISIBLE
                voteItemVisibility(View.VISIBLE, "Какое у Вас сегодня настроение?")
            }
        }.addOnFailureListener{
            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun checkTodayVote(){
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prefs.todayVote = false
                snapshot.children.forEach {
                    if(it.key == currentDate){
                        prefs.todayVote = true
                        return@forEach
                    }
                }

                if(prefs.todayVote){
                    binding.imgvVoteDone.alpha = 0f
                    binding.imgvVoteDone.scaleX = 0f
                    binding.imgvVoteDone.scaleY = 0f
                    binding.imgvVoteDone.visibility = View.VISIBLE
                    binding.imgvVoteDone.fadeIn(1000).subscribe()
                    voteItemVisibility(View.INVISIBLE, "Ответ дан!")
                }
                else{
                    binding.imgvVoteDone.visibility = View.INVISIBLE
                    voteItemVisibility(View.VISIBLE, "Какое у Вас сегодня настроение?")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        database.child("Учащиеся").child(prefs.myUUId).child("mood").addValueEventListener(listener)
    }

    fun View.fadeIn(duration: Long): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(this)
                    .setDuration(duration)
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }
    }

    fun View.zoomOut(duration: Long): Completable{
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        var centerY : Float = metrics.heightPixels.toFloat()/2
        val moodCenterPosition = centerY - this.y
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe{
            ViewCompat.animate(this)
                    .setDuration(duration)
                    .scaleXBy(1.1f)
                    .scaleYBy(1.1f)
                    .translationY(moodCenterPosition)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }
    }

    fun View.zoomIn(duration: Long): Completable{
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe{
            ViewCompat.animate(this)
                    .setDuration(duration)
                    .scaleX(0f)
                    .scaleY(0f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }
    }

}