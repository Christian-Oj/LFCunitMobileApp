package com.winners.lfcunit

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

data class User (
    var key:String = "",
    val email: String,
    val password: String,
    val username: String? = "",
    val first_name: String = "",
    val last_name:String = "",
    val title:String = "",
    val occupation:String = "",
    val phone_number:String = "",
    val gender:String = "",
    val education_level:String = "",
    val date_new_birth:String = "1900/01/01",
    val baptism_date:String = "1900/01/01",
    val BCC:Boolean = false,
    val LCC:Boolean = false,
    val LDC:Boolean = false,
    val unitLeader:Boolean = false,
    val creation_time:LocalDateTime = getCurrentTime(),
    val update_time:LocalDateTime = getCurrentTime()
)