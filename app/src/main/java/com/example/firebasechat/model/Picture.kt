package com.example.firebasechat.model

import android.support.v7.widget.RecyclerView

data class Picture(
    val id: Int,
    val path: String,
    val positionOnRecyclerView: Int,
    var isChecked: Boolean = false
)