package ru.netology.nmedia.repository

import android.text.method.TransformationMethod
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl: PostRepository {

    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }

                }

            })

    }

    override fun save(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

       client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

            })
    }

    override fun likeById(post: Post, callback: PostRepository.NMediaCallback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/posts/${post.id}/likes")
            .post(gson.toJson(post, Post::class.java).toRequestBody(jsonType))
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

            })
    }

    override fun removeLikeById(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}/api/posts/${post.id}/likes")
            .delete()
            .build()

        val response = client.newCall(request).execute()
        val responseText = response.body?.string() ?: error("Response body is null")


        return gson.fromJson(responseText, Post::class.java)
    }


    override fun shareById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id/share")
            .post("".toRequestBody())
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()

    }
}