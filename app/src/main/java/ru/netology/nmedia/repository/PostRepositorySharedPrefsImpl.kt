package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(
    context: Context
) : PostRepository {

    companion object {
        private const val KEY = "posts"
    }
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var nextId: Long = 0
    private var posts = emptyList<Post>()
    private var defaultPosts = listOf(
        Post(
            2,
            "Нетология. Университет интернет-профессий будущего",
            "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях",
            "18 сентября в 10:12",
            false,
            videoContent = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        ),
        Post(
            1,
            "Нетология. Университет интернет-профессий будущего",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb/",
            "21 Мая в 18:36",
            false,
            videoContent = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )
    )


    private val data = MutableLiveData(posts)

    init {
        prefs.getString(KEY, null)?.let {
            posts = gson.fromJson(it, typeToken)
            nextId = posts.maxOf { it.id } + 1
        } ?: run{
            posts = defaultPosts
            sync()
        }
        data.value = posts
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(KEY, gson.toJson(posts))
            apply()
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                amountLikes = if (it.likedByMe) it.amountLikes - 1 else it.amountLikes + 1

            )
        }
        data.value = posts
        sync()
    }



    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                amountShares = it.amountShares + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "Now"
                )
            ) + posts
        } else {
            posts.map { if (it.id == post.id) it.copy(content = post.content)
            else it }
        }
        data.value = posts
        sync()
    }

}