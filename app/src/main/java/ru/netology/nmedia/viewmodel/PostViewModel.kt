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
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.NMediaCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {

            }
        })
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContentAndSave(text: String, videoUrl: String) {

        val post = edited.value?.copy(content = text, videoContent = videoUrl)?: return

        repository.save(post, object : PostRepository.NMediaCallback<Post> {
            override fun onSuccess(post: Post) {
                _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().plus(post)))
                _postCreated.postValue(Unit)
            }

            override fun onError(e: Exception) {
                println("Error saving post: ${e.message}")
            }
        })

    }
    fun likeById(id: Long) {
        val post = _data.value?.posts.orEmpty().find { it.id == id }?: return

        val updatedPost = post.copy(likedByMe =!post.likedByMe, videoContent = post.videoContent?: "")
        _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map { if (it.id == id) updatedPost else it }))

        repository.likeById(updatedPost, object : PostRepository.NMediaCallback<Post> {
            override fun onSuccess(post: Post) {
                _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map { if (it.id == id) post else it }))
            }

            override fun onError(e: Exception) {
                println("Error liking post: ${e.message}")
                _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map { if (it.id == id) updatedPost else it }))
            }
        })
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