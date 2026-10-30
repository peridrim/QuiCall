package com.example.quicall

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavigationItem(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
)

val menuItems = listOf(
    NavigationItem(R.string.contacts_btn, R.drawable.ic_contacts),
    NavigationItem(R.string.add_contact_btn, R.drawable.ic_add),
    NavigationItem(R.string.dial_btn, R.drawable.ic_call)
)
