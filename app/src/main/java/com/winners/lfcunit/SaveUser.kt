package com.winners.lfcunit

import java.sql.Timestamp
import java.time.LocalDateTime

data class SaveUser(val key:String,
                    val email: String,
                    val password: String,
                    val username: String? = "",
                    val creation_time: LocalDateTime
)