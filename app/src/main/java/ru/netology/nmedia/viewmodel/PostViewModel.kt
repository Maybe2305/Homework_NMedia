package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    videoContent = ""
)
class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData<FeedModel>()
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    val edited = MutableLiveData(empty)

    init {
        load()
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))

            _data.postValue(
                try {
                    val posts = repository.getAll()
                    FeedModel(posts = posts, empty = posts.isEmpty())
                } catch (e: Exception) {
                    FeedModel(error = true)
                }
            )
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContentAndSave(text: String, videoUrl: String) {
        thread {
            edited.value?.let {
                val updatedPost = it.copy(content = text.trim(), videoContent = videoUrl)
                repository.save(updatedPost)
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }

    }
    fun likeById(id: Long) = thread {
        val post = _data.value?.posts?.find { it.id == id } ?: empty
        _data.postValue(_data.value?.copy(
            posts = _data.value?.posts.orEmpty().map {
                if (it.id == id) repository.likeById(post) else it
            }
        )
        )
    }
    fun shareById(id: Long) = thread { repository.shareById(id) }
    fun removeById(id: Long) = thread {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(
                posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
            )
        )
        try {
            repository.removeById(id)
        } catch (e: Exception) {
            _data.postValue(_data.value?.copy(posts = old))
        }

    }
    fun editDefault() {
        edited.value = empty
    }

}