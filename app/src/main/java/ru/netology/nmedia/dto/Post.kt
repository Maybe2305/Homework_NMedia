package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var amountLikes: Int = 0,
    val amountShares: Int = 0,
    val amountWatches: Int = 0,
    var videoContent: String,
)
