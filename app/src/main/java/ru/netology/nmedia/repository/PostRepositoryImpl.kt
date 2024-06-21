package ru.netology.nmedia.repository

import android.text.method.TransformationMethod
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl: PostRepository {


    override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {
        ApiService.service
            .getAll()
            .enqueue(object : Callback<List<Post>> {

                override fun onResponse(
                    call: Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ) {
                    if (response.isSuccessful) {
                        val body: List<Post> =
                            response.body() ?: throw RuntimeException("Body is null")
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException(response.message()))
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun likeById(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.likeById(post.id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: retrofit2.Response<Post>) {
                    val post = response.body() ?: throw RuntimeException("Response")
                    callback.onSuccess(post)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }


            })
    }

    override fun shareById(id: Long, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.shareById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: retrofit2.Response<Post>) {
                    val post = response.body() ?: throw RuntimeException("No post")
                    callback.onSuccess(post)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun removeById(id: Long, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {

                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun save(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.savePost(post)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: retrofit2.Response<Post>) {
                    val post = response.body() ?: throw RuntimeException("Invalid")
                    callback.onSuccess(post)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun removeLikeById(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        ApiService.service.unlikeById(post.id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: retrofit2.Response<Post>) {
                    val post = response.body()?: throw RuntimeException("No post")
                    callback.onSuccess(post)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }
}
