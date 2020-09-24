package com.winners.lfcunit

import java.sql.Timestamp

data class User(
    val email: String,
    val password: String,
    val username: String? = "",
    val unitLeader:Boolean = false
)