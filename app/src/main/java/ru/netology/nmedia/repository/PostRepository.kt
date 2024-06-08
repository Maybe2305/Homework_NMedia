package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: NMediaCallback<List<Post>>)
    fun likeById(post: Post, callback: NMediaCallback<Post>)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post, callback: NMediaCallback<Post>)
    fun removeLikeById(post: Post): Post

    interface NMediaCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }
}