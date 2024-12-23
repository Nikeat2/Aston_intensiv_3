package com.example.astonhomework3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contacts(
    val id: Int,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val isChecked: Boolean = false
) : Parcelable
