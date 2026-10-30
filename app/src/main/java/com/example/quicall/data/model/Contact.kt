package com.example.quicall.data.model

import androidx.annotation.DrawableRes

data class Contact(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val isFavorite: Boolean = false,
    @DrawableRes val imageResId: Int? = null
)
