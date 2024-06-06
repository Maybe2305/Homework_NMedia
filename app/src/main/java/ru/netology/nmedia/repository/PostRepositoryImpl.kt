package ru.netology.nmedia.repository

import android.text.method.TransformationMethod
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import java.util.concurrent.TimeUnit

class PostRepositoryImpl: PostRepository {

    val postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()

    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        val response = client.newCall(request)
            .execute()

        val responseText = response.body?.string() ?: error("Response body is null")

        return gson.fromJson(responseText, type)
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        val response = client.newCall(request)
            .execute()

        val responseText = response.body?.string() ?: error("Response body is null")

        return gson.fromJson(responseText, Post::class.java)
    }

    override fun likeById(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}/api/posts/${post.id}/likes")
            .post(gson.toJson(post, Post::class.java).toRequestBody(jsonType))
            .build()

        val response = client.newCall(request).execute()
        val responseText = response.body?.string() ?: error("Response body is null")
        return gson.fromJson(responseText, Post::class.java)
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