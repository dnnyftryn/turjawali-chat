package com.aplikasi.turjawalichat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val name: String? = null,
    val email: String? = null,
    val uid: String? = null,
) : Parcelable