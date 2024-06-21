package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: NMediaCallback<List<Post>>)
    fun likeById(post: Post, callback: NMediaCallback<Post>)
    fun shareById(id: Long, callback: NMediaCallback<Post>)
    fun removeById(id: Long, callback: NMediaCallback<Post>)
    fun save(post: Post, callback: NMediaCallback<Post>)
    fun removeLikeById(post: Post, callback: NMediaCallback<Post>)

    interface NMediaCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }
}