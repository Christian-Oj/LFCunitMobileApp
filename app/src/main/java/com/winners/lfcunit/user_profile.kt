package com.winners.lfcunit

data class user_profile(val firstname:String?="",
                        val lastname: String? ="",
                        val phone:String?="",
                        val fullName:String = "${lastname} ${firstname}"
)