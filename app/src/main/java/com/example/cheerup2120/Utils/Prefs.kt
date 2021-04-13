package com.example.cheerup2120.Utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) {

    companion object{
        private const val PREFS_FILENAME = "myPrefs"
        private const val KEY_UUID = "myUUID"
        private const val KEY_TEACHER_EMAIL = "teacherEmail"
        private const val KEY_STUDENT_FIO = "studentFIO"
        private const val KEY_STUDENT_CLASS = "studentClass"
        private const val KEY_VOTE = "todayVote"
        private const val KEY_STUDENT_CORPUSE = "studentCorpus"
        private const val KEY_STUDENT_CLASS_LETTER = "studentClassLetter"
    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var myUUId: String
        get() = sharedPref.getString(KEY_UUID, "") ?: ""
        set(value) = sharedPref.edit{ putString(KEY_UUID, value)}
    var teacherEmail: String
        get() = sharedPref.getString(KEY_TEACHER_EMAIL, "") ?: ""
        set(value) = sharedPref.edit{ putString(KEY_TEACHER_EMAIL, value)}
    var studentFIO: String
        get() = sharedPref.getString(KEY_STUDENT_FIO, "") ?: ""
        set(value) = sharedPref.edit{ putString(KEY_STUDENT_FIO, value)}
    var studentClass: String
        get() = sharedPref.getString(KEY_STUDENT_CLASS, "") ?: ""
        set(value) = sharedPref.edit{ putString(KEY_STUDENT_CLASS, value)}
    var todayVote: Boolean
        get() = sharedPref.getBoolean(KEY_VOTE, false) ?: false
        set(value) = sharedPref.edit { putBoolean(KEY_VOTE, value)}
    var studentCorpus: String
        get() = sharedPref.getString(KEY_STUDENT_CORPUSE, "") ?: ""
        set(value) = sharedPref.edit{ putString(KEY_STUDENT_CORPUSE, value)}
    var studentClassLetter: String
        get() = sharedPref.getString(KEY_STUDENT_CLASS_LETTER, "") ?: ""
        set(value) = sharedPref.edit{ putString(KEY_STUDENT_CLASS_LETTER, value)}
}