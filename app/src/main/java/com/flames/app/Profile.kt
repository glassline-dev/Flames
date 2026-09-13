package com.flames.app

data class Profile(
    val name: String,
    val age: Int,
    val distance: String,
    val bio: String,
    val interests: List<String>,
    val initials: String,
    val accent: Int
)
