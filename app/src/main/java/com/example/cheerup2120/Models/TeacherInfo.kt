package com.example.cheerup2120.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TeacherInfo(
        var teacherEmail: String? = "",
        var corpuses: String? = ""
){
    fun toMap(): Map<String, Any?>{
        return mapOf(
                "teacherEmail" to teacherEmail,
                "corpuses" to corpuses
        )
    }
}
