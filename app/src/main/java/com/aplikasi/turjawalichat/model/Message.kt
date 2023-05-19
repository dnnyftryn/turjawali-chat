package com.aplikasi.turjawalichat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message (
    val message: String? = null,
    val senderId: String? = null,
    val timestamp: Long? = null,
): Parcelable