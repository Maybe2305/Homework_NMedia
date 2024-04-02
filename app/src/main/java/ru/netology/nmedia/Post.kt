package ru.netology.nmedia

import android.widget.TextView

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var amountLikes: Int = 999,
    val amountShares: Int = 997,
    val amountWatches: Int = 150000,
)
